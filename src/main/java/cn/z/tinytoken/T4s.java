package cn.z.tinytoken;

import cn.z.id.Id;
import cn.z.tinytoken.autoconfigure.TinyTokenAuthProperties;
import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * <h1>轻量级权限认证Spring实现</h1>
 *
 * <h3>token类型:String</h3>
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
     * 轻量级权限认证验证配置属性
     */
    private final TinyTokenAuthProperties tinyTokenAuthProperties;
    /**
     * Redis模板
     */
    private final Rt rt;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties     TinyTokenProperties
     * @param tinyTokenAuthProperties TinyTokenAuthProperties
     * @param rt                      Rt
     */
    public T4s(TinyTokenProperties tinyTokenProperties, TinyTokenAuthProperties tinyTokenAuthProperties, Rt rt) {
        this.tinyTokenProperties = tinyTokenProperties;
        this.tinyTokenAuthProperties = tinyTokenAuthProperties;
        this.rt = rt;
    }

    /**
     * 用户名和密码是否正确
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户名和密码正确
     */
    public boolean isCorrect(String username, String password) {
        return Objects.equals(username, tinyTokenAuthProperties.getUsername()) && Objects.equals(password, tinyTokenAuthProperties.getPassword());
    }

    /**
     * 设置token(token使用16位随机字符串 过期时间使用默认值)
     *
     * @return token
     */
    public String setToken() {
        String token = encode(Id.next());
        setToken(token, tinyTokenProperties.getTimeout());
        return token;
    }

    /**
     * 设置token(token使用16位随机字符串)
     *
     * @param timeout 过期时间(秒)
     * @return token
     */
    public String setToken(long timeout) {
        String token = encode(Id.next());
        setToken(token, timeout);
        return token;
    }

    /**
     * 设置token
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     */
    public void setToken(String token, long timeout) {
        rt.set(tinyTokenProperties.getPrefix() + ":" + token, "", timeout);
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
        return rt.exists(tinyTokenProperties.getPrefix() + ":" + token);
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
        return rt.delete(tinyTokenProperties.getPrefix() + ":" + token);
    }

    /**
     * 设置过期时间(当前Context 过期时间使用默认值)
     *
     * @return 是否成功
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
     */
    public Boolean expire(String token) {
        return expire(token, tinyTokenProperties.getTimeout());
    }

    /**
     * 设置过期时间
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    public Boolean expire(String token, long timeout) {
        return rt.expire(tinyTokenProperties.getPrefix() + ":" + token, timeout);
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
        return rt.persist(tinyTokenProperties.getPrefix() + ":" + token);
    }

    /**
     * 获取信息(当前Context)
     *
     * @return token, 过期时间(秒)[-1:不过期]<br>
     * (不存在返回null)
     */
    public Map.Entry<String, Long> getInfoByToken() {
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
     * @return token, 过期时间(秒)[-1:不过期]<br>
     * (不存在返回null)
     * @since 1.6.1
     */
    public Map.Entry<String, Long> getInfoByToken(HttpServletRequest request) {
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
     * @return token, 过期时间(秒)[-1:不过期]<br>
     * (不存在返回null)
     */
    public Map.Entry<String, Long> getInfoByToken(String token) {
        long expire = rt.getExpire(tinyTokenProperties.getPrefix() + ":" + token);
        if (expire > -2) {
            return new AbstractMap.SimpleEntry<>(token, expire);
        }
        return null;
    }

    /**
     * 获取所有键列表
     *
     * @return 键列表(不存在返回[])
     */
    private List<String> getKey() {
        return rt.scan(tinyTokenProperties.getPrefix() + ":*");
    }

    /**
     * 获取token列表
     *
     * @return token列表(不存在返回[])
     */
    public List<String> getTokenList() {
        List<String> list = new ArrayList<>();
        List<String> keys = getKey();
        for (String key : keys) {
            String[] split = key.split(":", -1);
            if (split.length == 2) {
                list.add(split[1]);
            }
        }
        return list;
    }

    /**
     * 获取所有信息列表
     *
     * @return token, 过期时间(秒)[-1:不过期]列表<br>
     * (不存在返回[])
     */
    public List<Map.Entry<String, Long>> getInfo() {
        List<Map.Entry<String, Long>> list = new ArrayList<>();
        List<String> keys = getKey();
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 2) {
                    list.add(new AbstractMap.SimpleEntry<>(split[1], expire));
                }
            }
        }
        return list;
    }

    /**
     * 获取所有永不过期信息列表
     *
     * @return token列表(不存在返回[])
     */
    public List<String> getInfoPersist() {
        List<String> list = new ArrayList<>();
        List<String> keys = getKey();
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire == -1) {
                String[] split = key.split(":", -1);
                if (split.length == 2) {
                    list.add(split[1]);
                }
            }
        }
        return list;
    }

    /**
     * 生成16位随机字符串
     *
     * @param n 数字
     * @return 16位随机字符串
     */
    public static String encode(long n) {
        StringBuilder result = new StringBuilder(16);
        StringBuilder random = new StringBuilder(10);
        for (int i = 0; i < 5; i++) {
            char r = (char) Base62.ALPHABET[RANDOM.nextInt(62)];
            result.append(r);
            random.append(r);
            random.append((char) Base62.ALPHABET[14 * i + 4]);
        }
        String number = Base62.encode(n ^ Base62.decode(random.toString()));
        char zero = (char) Base62.ALPHABET[0];
        for (int i = 0; i < 11 - number.length(); i++) {
            result.append(zero);
        }
        result.append(number);
        return result.toString();
    }

    /**
     * 解析16位随机字符串
     *
     * @param s 16位随机字符串
     * @return [0] timestamp 时间戳<br>
     * [1] machineId 机器码<br>
     * [2] sequence  序列号
     */
    public static long[] decode(String s) {
        StringBuilder random = new StringBuilder(10);
        for (int i = 0; i < 5; i++) {
            random.append(s.charAt(i));
            random.append((char) Base62.ALPHABET[14 * i + 4]);
        }
        return Id.parse(Base62.decode(random.toString()) ^ Base62.decode(s.substring(5, 16)));
    }

}
