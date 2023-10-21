package cn.z.tinytoken;

import cn.z.id.Id;
import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import cn.z.tinytoken.entity.TokenInfo;
import cn.z.tinytoken.entity.TokenInfoExtra;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
     * 前缀
     */
    private final String prefix;
    /**
     * 过期时间(秒)
     */
    private final long timeout;
    /**
     * Redis模板类
     */
    private final Rt rt;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties TinyTokenProperties
     * @param rt                  Rt
     */
    public T4s(TinyTokenProperties tinyTokenProperties, Rt rt) {
        this.prefix = tinyTokenProperties.getPrefix();
        this.timeout = tinyTokenProperties.getTimeout();
        this.rt = rt;
    }

    /**
     * 获取前缀
     *
     * @return 前缀
     * @since 1.2.0
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 获取过期时间(秒)
     *
     * @return 过期时间(秒)
     * @since 1.2.0
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置token(token使用32位随机字符串 过期时间使用默认值)
     *
     * @param id id
     * @return token
     */
    public String setToken(long id) {
        String token = encode(Id.next(), id);
        setToken(id, token, timeout);
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
        setToken(id, token, timeout);
    }

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param timeout 过期时间(秒)
     */
    public void setToken(long id, String token, long timeout) {
        rt.set(prefix + ":" + id + ":" + token, "", timeout);
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
        rt.set(prefix + ":" + id + ":" + token, extra, timeout);
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
        return ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(prefix);
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
     * 获取键
     *
     * @param token token
     * @return 键(不存在返回null)
     */
    private String getKey(String token) {
        List<String> scan = rt.scan(prefix + ":*:" + token);
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
        return rt.scan(prefix + ":" + id + ":*");
    }

    /**
     * 获取所有键列表
     *
     * @return 键列表(不存在返回[])
     * @since 1.2.0
     */
    private List<String> getKey() {
        return rt.scan(prefix + ":*:*");
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
     * @param token token
     * @return 是否存在
     */
    public boolean existByToken(String token) {
        return !rt.scan(prefix + ":*:" + token).isEmpty();
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
     * @param time 时间戳
     * @param id   id
     * @return 32位随机字符串
     */
    public static String encode(long time, long id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) Base62.ALPHABET[RANDOM.nextInt(62)]);
        }
        String er = sb.toString();
        long random = Base62.decode(er);
        long tr = time ^ random;
        long ti = time ^ id;
        StringBuilder etr = new StringBuilder(Base62.encode(tr));
        StringBuilder eti = new StringBuilder(Base62.encode(ti));
        for (int i = 0; i < 11 - etr.length(); i++) {
            etr.insert(0, (char) Base62.ALPHABET[0]);
        }
        for (int i = 0; i < 11 - eti.length(); i++) {
            eti.insert(0, (char) Base62.ALPHABET[0]);
        }
        return er + etr + eti;
    }

    /**
     * 解析32位随机字符串
     *
     * @param string 32位随机字符串
     * @return [0] timestamp 时间戳<br>
     * [1] machineId 机器码<br>
     * [2] sequence  序列号<br>
     * [3] id  id
     * @since 1.2.1
     */
    public static long[] decode(String string) {
        long dr = Base62.decode(string.substring(0, 10));
        long dtr = Base62.decode(string.substring(10, 21));
        long dti = Base62.decode(string.substring(21, 32));
        long dt = dr ^ dtr;
        long di = dt ^ dti;
        long[] parse = Id.parse(dt);
        return new long[]{parse[0], parse[1], parse[2], di};
    }

}
