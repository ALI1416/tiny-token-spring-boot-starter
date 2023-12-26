package cn.z.tinytoken;

import cn.z.id.Id;
import cn.z.tinytoken.autoconfigure.TinyTokenAuthProperties;
import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
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
     * header
     */
    private final String header;
    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 过期时间(秒)
     */
    private final long timeout;
    /**
     * 轻量级权限认证验证配置属性
     */
    private final TinyTokenAuthProperties tinyTokenAuthProperties;
    /**
     * Redis模板类
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
        this.header = tinyTokenProperties.getHeader();
        this.prefix = tinyTokenProperties.getPrefix();
        this.timeout = tinyTokenProperties.getTimeout();
        this.tinyTokenAuthProperties = tinyTokenAuthProperties;
        this.rt = rt;
    }

    /**
     * 获取header
     *
     * @return 前缀
     */
    public String getHeader() {
        return header;
    }

    /**
     * 获取前缀
     *
     * @return 前缀
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 获取过期时间(秒)
     *
     * @return 过期时间(秒)
     */
    public long getTimeout() {
        return timeout;
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
        setToken(token, timeout);
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
        rt.set(prefix + ":" + token, "", timeout);
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
        return ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(header);
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
        return rt.exists(prefix + ":" + token);
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
        return rt.delete(prefix + ":" + token);
    }

    /**
     * 设置过期时间(当前Context 过期时间使用默认值)
     *
     * @return 是否成功
     */
    public Boolean expire() {
        String token = getToken();
        if (token != null) {
            return expire(token, timeout);
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
     * 设置过期时间(过期时间使用默认值)
     *
     * @param token token
     * @return 是否成功
     */
    public Boolean expire(String token) {
        return expire(token, timeout);
    }

    /**
     * 设置过期时间
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    public Boolean expire(String token, long timeout) {
        return rt.expire(prefix + ":" + token, timeout);
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
        return rt.persist(prefix + ":" + token);
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
     * @param token token
     * @return token, 过期时间(秒)[-1:不过期]<br>
     * (不存在返回null)
     */
    public Map.Entry<String, Long> getInfoByToken(String token) {
        long expire = rt.getExpire(prefix + ":" + token);
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
        return rt.scan(prefix + ":*");
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
     * @return token, 过期时间(秒)[-1:不过期]<br>
     * (不存在返回空map)
     */
    public Map<String, Long> getInfo() {
        Map<String, Long> map = new HashMap<>();
        List<String> keys = getKey();
        for (String key : keys) {
            long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 2) {
                    map.put(split[1], expire);
                }
            }
        }
        return map;
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
     * @param time 时间戳
     * @return 16位随机字符串
     */
    public static String encode(long time) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append((char) Base62.ALPHABET[RANDOM.nextInt(62)]);
        }
        String er = sb.toString();
        long random = Base62.decode(er);
        long tr = time ^ random;
        StringBuilder etr = new StringBuilder(Base62.encode(tr));
        int etrL = 11 - etr.length();
        for (int i = 0; i < etrL; i++) {
            etr.insert(0, (char) Base62.ALPHABET[0]);
        }
        return er + etr;
    }

    /**
     * 解析16位随机字符串
     *
     * @param string 16位随机字符串
     * @return [0] timestamp 时间戳<br>
     * [1] machineId 机器码<br>
     * [2] sequence  序列号
     */
    public static long[] decode(String string) {
        long dr = Base62.decode(string.substring(0, 5));
        long dtr = Base62.decode(string.substring(5, 16));
        long dt = dr ^ dtr;
        return Id.parse(dt);
    }

}
