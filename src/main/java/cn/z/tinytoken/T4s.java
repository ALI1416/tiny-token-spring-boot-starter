package cn.z.tinytoken;

import cn.z.id.Id;
import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import cn.z.tinytoken.entity.TokenInfo;
import cn.z.tinytoken.entity.TokenInfoExtra;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <h1>轻量级权限认证Spring实现</h1>
 *
 * <h3>id类型:long</h3>
 * <h3>token类型:String</h3>
 * <h3>拓展内容类型:String</h3>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class T4s {

    /**
     * 随机数实例
     */
    private static final Random RANDOM = new Random();
    /**
     * 轻量级权限认证配置属性
     */
    private final TinyTokenProperties tinyTokenProperties;
    /**
     * Redis模板
     */
    private final Rt rt;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties TinyTokenProperties
     * @param rt                  Rt
     */
    public T4s(TinyTokenProperties tinyTokenProperties, Rt rt) {
        this.tinyTokenProperties = tinyTokenProperties;
        this.rt = rt;
    }

    /**
     * 设置token(token使用32位随机字符串 过期时间使用默认值)
     *
     * @param id id
     * @return token
     */
    public String setToken(long id) {
        String token = encode(Id.next(), id);
        setToken(id, token, tinyTokenProperties.getTimeout());
        return token;
    }

    /**
     * 设置token(token使用32位随机字符串)
     *
     * @param id      id
     * @param timeout 过期时间(秒)
     * @return token
     */
    public String setToken(long id, long timeout) {
        String token = encode(Id.next(), id);
        setToken(id, token, timeout);
        return token;
    }

    /**
     * 设置token(过期时间使用默认值)
     *
     * @param id    id
     * @param token token
     */
    public void setToken(long id, String token) {
        setToken(id, token, tinyTokenProperties.getTimeout());
    }

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param timeout 过期时间(秒)
     */
    public void setToken(long id, String token, long timeout) {
        rt.set(tinyTokenProperties.getPrefix() + ":" + id + ":" + token, "", timeout);
    }

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)
     */
    public void setToken(long id, String token, String extra, long timeout) {
        rt.set(tinyTokenProperties.getPrefix() + ":" + id + ":" + token, extra, timeout);
    }

    /**
     * 设置拓展内容(当前Context)
     *
     * @param extra 拓展内容
     * @return 是否成功
     * @since 1.4.1
     */
    public boolean setExtra(String extra) {
        String token = getToken();
        if (token != null) {
            return setExtra(token, extra);
        }
        return false;
    }

    /**
     * 设置拓展内容
     *
     * @param request HttpServletRequest
     * @param extra   拓展内容
     * @return 是否成功
     * @since 1.6.1
     */
    public boolean setExtra(HttpServletRequest request, String extra) {
        String token = getToken(request);
        if (token != null) {
            return setExtra(token, extra);
        }
        return false;
    }

    /**
     * 设置拓展内容
     *
     * @param token token
     * @param extra 拓展内容
     * @return 是否成功
     * @since 1.2.0
     */
    public boolean setExtra(String token, String extra) {
        String key = getKey(token);
        if (key != null) {
            long expire = rt.getExpire(key);
            if (expire == -1) {
                rt.set(key, extra);
            } else {
                rt.set(key, extra, expire);
            }
            return true;
        }
        return false;
    }

    /**
     * 清除拓展内容(当前Context)
     *
     * @return 是否成功
     * @since 1.4.1
     */
    public boolean clearExtra() {
        String token = getToken();
        if (token != null) {
            return clearExtra(token);
        }
        return false;
    }

    /**
     * 清除拓展内容
     *
     * @param request HttpServletRequest
     * @return 是否成功
     * @since 1.6.1
     */
    public boolean clearExtra(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return clearExtra(token);
        }
        return false;
    }

    /**
     * 清除拓展内容
     *
     * @param token token
     * @return 是否成功
     * @since 1.2.0
     */
    public boolean clearExtra(String token) {
        String key = getKey(token);
        if (key != null) {
            long expire = rt.getExpire(key);
            if (expire == -1) {
                rt.set(key, "");
            } else {
                rt.set(key, "", expire);
            }
            return true;
        }
        return false;
    }

    /**
     * 获取token(当前Context 不判断是否有效)
     *
     * @return token(不存在返回null)
     * @throws TinyTokenException 不存在Context
     */
    public String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new TinyTokenException("不存在Context");
        }
        return getToken(((ServletRequestAttributes) requestAttributes).getRequest());
    }

    /**
     * 获取token(不判断是否有效)
     *
     * @param request HttpServletRequest
     * @return token(不存在返回null)
     * @since 1.6.1
     */
    public String getToken(HttpServletRequest request) {
        return request.getHeader(tinyTokenProperties.getHeader());
    }

    /**
     * 获取token(当前Context 判断是否有效)
     *
     * @return token(不存在或无效返回null)
     */
    public String getTokenValid() {
        String token = getToken();
        if (token != null && existByToken(token)) {
            return token;
        }
        return null;
    }

    /**
     * 获取token(判断是否有效)
     *
     * @param request HttpServletRequest
     * @return token(不存在或无效返回null)
     * @since 1.6.1
     */
    public String getTokenValid(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null && existByToken(token)) {
            return token;
        }
        return null;
    }

    /**
     * 获取键
     *
     * @param token token
     * @return 键(不存在返回null)
     */
    private String getKey(String token) {
        List<String> scan = rt.scan(tinyTokenProperties.getPrefix() + ":*:" + token);
        if (!scan.isEmpty()) {
            return scan.get(0);
        }
        return null;
    }

    /**
     * 获取键列表
     *
     * @param id id
     * @return 键列表(不存在返回[])
     */
    private List<String> getKey(long id) {
        return rt.scan(tinyTokenProperties.getPrefix() + ":" + id + ":*");
    }

    /**
     * 获取所有键列表
     *
     * @return 键列表(不存在返回[])
     * @since 1.2.0
     */
    private List<String> getKey() {
        return rt.scan(tinyTokenProperties.getPrefix() + ":*:*");
    }

    /**
     * 获取token列表
     *
     * @param id id
     * @return token列表(不存在返回[])
     */
    public List<String> getToken(long id) {
        List<String> tokens = new ArrayList<>();
        List<String> keys = getKey(id);
        for (String key : keys) {
            String[] split = key.split(":", -1);
            if (split.length == 3) {
                tokens.add(split[2]);
            }
        }
        return tokens;
    }

    /**
     * 获取id(当前Context)
     *
     * @return id(不存在返回null)
     */
    public Long getId() {
        String token = getToken();
        if (token != null) {
            return getId(token);
        }
        return null;
    }

    /**
     * 获取id
     *
     * @param request HttpServletRequest
     * @return id(不存在返回null)
     * @since 1.6.1
     */
    public Long getId(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return getId(token);
        }
        return null;
    }

    /**
     * 获取id
     *
     * @param token token
     * @return id(不存在返回null)
     */
    public Long getId(String token) {
        String key = getKey(token);
        if (key != null) {
            String[] split = key.split(":", -1);
            if (split.length == 3) {
                return Long.parseLong(split[1]);
            }
        }
        return null;
    }

    /**
     * 获取拓展内容(当前Context)
     *
     * @return 拓展内容(不存在返回null)
     * @since 1.4.1
     */
    public String getExtra() {
        String token = getToken();
        if (token != null) {
            return getExtra(token);
        }
        return null;
    }

    /**
     * 获取拓展内容
     *
     * @param request HttpServletRequest
     * @return 拓展内容(不存在返回null)
     * @since 1.6.1
     */
    public String getExtra(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return getExtra(token);
        }
        return null;
    }

    /**
     * 获取拓展内容
     *
     * @param token token
     * @return 拓展内容(不存在返回null)
     * @since 1.4.1
     */
    public String getExtra(String token) {
        String key = getKey(token);
        if (key != null) {
            return (String) rt.get(key);
        }
        return null;
    }

    /**
     * token是否存在(当前Context)
     *
     * @return 是否存在
     */
    public boolean existByToken() {
        return getTokenValid() != null;
    }

    /**
     * token是否存在
     *
     * @param request HttpServletRequest
     * @return 是否存在
     * @since 1.6.1
     */
    public boolean existByToken(HttpServletRequest request) {
        return getTokenValid(request) != null;
    }

    /**
     * token是否存在
     *
     * @param token token
     * @return 是否存在
     */
    public boolean existByToken(String token) {
        return !rt.scan(tinyTokenProperties.getPrefix() + ":*:" + token).isEmpty();
    }

    /**
     * id是否存在
     *
     * @param id id
     * @return 是否存在
     */
    public boolean existById(long id) {
        return !getKey(id).isEmpty();
    }

    /**
     * 删除(当前Context)
     *
     * @return 是否成功
     */
    public Boolean deleteByToken() {
        String token = getToken();
        if (token != null) {
            return deleteByToken(token);
        }
        return false;
    }

    /**
     * 删除
     *
     * @param request HttpServletRequest
     * @return 是否成功
     * @since 1.6.1
     */
    public Boolean deleteByToken(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return deleteByToken(token);
        }
        return false;
    }

    /**
     * 删除
     *
     * @param token token
     * @return 是否成功
     */
    public Boolean deleteByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.delete(key);
        }
        return false;
    }

    /**
     * 删除
     *
     * @param id id
     * @return 成功个数
     */
    public Long deleteById(long id) {
        List<String> keys = getKey(id);
        if (!keys.isEmpty()) {
            return rt.deleteMulti(keys);
        }
        return 0L;
    }

    /**
     * 设置过期时间(当前Context 过期时间使用默认值)
     *
     * @return 是否成功
     * @since 1.2.3
     */
    public Boolean expire() {
        String token = getToken();
        if (token != null) {
            return expire(token, tinyTokenProperties.getTimeout());
        }
        return false;
    }

    /**
     * 设置过期时间(过期时间使用默认值)
     *
     * @param request HttpServletRequest
     * @return 是否成功
     * @since 1.6.1
     */
    public Boolean expire(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return expire(token, tinyTokenProperties.getTimeout());
        }
        return false;
    }

    /**
     * 设置过期时间(当前Context)
     *
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    public Boolean expire(long timeout) {
        String token = getToken();
        if (token != null) {
            return expire(token, timeout);
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param request HttpServletRequest
     * @param timeout 过期时间(秒)
     * @return 是否成功
     * @since 1.6.1
     */
    public Boolean expire(HttpServletRequest request, long timeout) {
        String token = getToken(request);
        if (token != null) {
            return expire(token, timeout);
        }
        return false;
    }

    /**
     * 设置过期时间(过期时间使用默认值)
     *
     * @param token token
     * @return 是否成功
     * @since 1.2.3
     */
    public Boolean expire(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.expire(key, tinyTokenProperties.getTimeout());
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    public Boolean expire(String token, long timeout) {
        String key = getKey(token);
        if (key != null) {
            return rt.expire(key, timeout);
        }
        return false;
    }

    /**
     * 设置永不过期(当前Context)
     *
     * @return 是否成功
     */
    public Boolean persist() {
        String token = getToken();
        if (token != null) {
            return persist(token);
        }
        return false;
    }

    /**
     * 设置永不过期
     *
     * @param request HttpServletRequest
     * @return 是否成功
     * @since 1.6.1
     */
    public Boolean persist(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return persist(token);
        }
        return false;
    }

    /**
     * 设置永不过期
     *
     * @param token token
     * @return 是否成功
     */
    public Boolean persist(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.persist(key);
        }
        return false;
    }

    /**
     * 获取信息(当前Context)
     *
     * @return 信息(不存在返回null)
     */
    public TokenInfo getInfoByToken() {
        String token = getToken();
        if (token != null) {
            return getInfoByToken(token);
        }
        return null;
    }

    /**
     * 获取信息
     *
     * @param request HttpServletRequest
     * @return 信息(不存在返回null)
     * @since 1.6.1
     */
    public TokenInfo getInfoByToken(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return getInfoByToken(token);
        }
        return null;
    }

    /**
     * 获取信息
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    public TokenInfo getInfoByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    return new TokenInfo(Long.parseLong(split[1]), token, expire);
                }
            }
        }
        return null;
    }

    /**
     * 获取信息列表
     *
     * @param id id
     * @return 信息列表(不存在返回[])
     */
    public List<TokenInfo> getInfoById(long id) {
        List<TokenInfo> list = new ArrayList<>();
        List<String> keys = getKey(id);
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    list.add(new TokenInfo(id, split[2], expire));
                }
            }
        }
        return list;
    }

    /**
     * 获取所有信息列表
     *
     * @return 信息列表(不存在返回[])
     * @since 1.2.0
     */
    public List<TokenInfo> getInfo() {
        List<TokenInfo> list = new ArrayList<>();
        List<String> keys = getKey();
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    list.add(new TokenInfo(Long.parseLong(split[1]), split[2], expire));
                }
            }
        }
        return list;
    }

    /**
     * 获取所有永不过期信息列表
     *
     * @return 信息列表(不存在返回[])
     * @since 1.2.0
     */
    public List<TokenInfo> getInfoPersist() {
        List<TokenInfo> list = new ArrayList<>();
        List<String> keys = getKey();
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire == -1) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    list.add(new TokenInfo(Long.parseLong(split[1]), split[2], expire));
                }
            }
        }
        return list;
    }

    /**
     * 获取信息拓展(当前Context)
     *
     * @return 信息拓展(不存在返回null)
     */
    public TokenInfoExtra getInfoExtraByToken() {
        String token = getToken();
        if (token != null) {
            return getInfoExtraByToken(token);
        }
        return null;
    }

    /**
     * 获取信息拓展
     *
     * @param request HttpServletRequest
     * @return 信息拓展(不存在返回null)
     * @since 1.6.1
     */
    public TokenInfoExtra getInfoExtraByToken(HttpServletRequest request) {
        String token = getToken(request);
        if (token != null) {
            return getInfoExtraByToken(token);
        }
        return null;
    }

    /**
     * 获取信息拓展
     *
     * @param token token
     * @return 信息拓展(不存在返回null)
     */
    public TokenInfoExtra getInfoExtraByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    return new TokenInfoExtra(Long.parseLong(split[1]), token, (String) rt.get(key), expire);
                }
            }
        }
        return null;
    }

    /**
     * 获取信息拓展列表
     *
     * @param id id
     * @return 信息拓展列表(不存在返回[])
     */
    public List<TokenInfoExtra> getInfoExtraById(long id) {
        List<TokenInfoExtra> list = new ArrayList<>();
        List<String> keys = getKey(id);
        if (!keys.isEmpty()) {
            List<Object> extras = rt.getMulti(keys);
            for (int i = 0; i < keys.size(); i++) {
                long expire = rt.getExpire(keys.get(i));
                if (expire > -2) {
                    String[] split = keys.get(i).split(":", -1);
                    if (split.length == 3) {
                        list.add(new TokenInfoExtra(id, split[2], (String) extras.get(i), expire));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取所有信息拓展列表
     *
     * @return 信息拓展列表(不存在返回[])
     * @since 1.2.0
     */
    public List<TokenInfoExtra> getInfoExtra() {
        List<TokenInfoExtra> list = new ArrayList<>();
        List<String> keys = getKey();
        if (!keys.isEmpty()) {
            List<Object> extras = rt.getMulti(keys);
            for (int i = 0; i < keys.size(); i++) {
                long expire = rt.getExpire(keys.get(i));
                if (expire > -2) {
                    String[] split = keys.get(i).split(":", -1);
                    if (split.length == 3) {
                        list.add(new TokenInfoExtra(Long.parseLong(split[1]), split[2], (String) extras.get(i), expire));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取所有永不过期信息拓展列表
     *
     * @return 信息拓展列表(不存在返回[])
     * @since 1.2.0
     */
    public List<TokenInfoExtra> getInfoExtraPersist() {
        List<TokenInfoExtra> list = new ArrayList<>();
        List<String> keys = getKey();
        if (!keys.isEmpty()) {
            List<Object> extras = rt.getMulti(keys);
            for (int i = 0; i < keys.size(); i++) {
                long expire = rt.getExpire(keys.get(i));
                if (expire == -1) {
                    String[] split = keys.get(i).split(":", -1);
                    if (split.length == 3) {
                        list.add(new TokenInfoExtra(Long.parseLong(split[1]), split[2], (String) extras.get(i), expire));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 生成32位随机字符串
     *
     * @param n  数字
     * @param id id
     * @return 32位随机字符串
     * @since 1.2.1
     */
    public static String encode(long n, long id) {
        StringBuilder result = new StringBuilder(32);
        for (int i = 0; i < 10; i++) {
            result.append((char) Base62.ALPHABET[RANDOM.nextInt(62)]);
        }
        String number = Base62.encode(n ^ Base62.decode(result.toString()));
        String encode = Base62.encode(n ^ id);
        char zero = (char) Base62.ALPHABET[0];
        for (int i = 0; i < 11 - number.length(); i++) {
            result.append(zero);
        }
        result.append(number);
        for (int i = 0; i < 11 - encode.length(); i++) {
            result.append(zero);
        }
        result.append(encode);
        return result.toString();
    }

    /**
     * 解析32位随机字符串
     *
     * @param s 32位随机字符串
     * @return [0] timestamp 时间戳<br>
     * [1] machineId 机器码<br>
     * [2] sequence  序列号<br>
     * [3] id  id
     * @since 1.2.1
     */
    public static long[] decode(String s) {
        long n = Base62.decode(s.substring(0, 10)) ^ Base62.decode(s.substring(10, 21));
        long[] parse = Id.parse(n);
        return new long[]{parse[0], parse[1], parse[2], n ^ Base62.decode(s.substring(21, 32))};
    }

}
