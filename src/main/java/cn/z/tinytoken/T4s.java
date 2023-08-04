package cn.z.tinytoken;

import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

/**
 * <h1>轻量级权限认证Spring实现</h1>
 *
 * <h3>token类型:String</h3>
 * <h3>id类型:Long</h3>
 * <h3>拓展内容类型:Void</h3>
 *
 * <p>
 * 使用Redis储存token<br>
 * 键名：前缀:id:token<br>
 * 值：空字符串
 * </p>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Component
public class T4s implements TinyToken<String, Long, Void> {

    /**
     * 名称
     */
    private final String name;
    /**
     * 过期时间(秒)
     */
    private final Long timeout;

    private final Rt rt;

    /**
     * 构造函数
     *
     * @param tinyTokenProperties TinyTokenProperties
     */
    public T4s(TinyTokenProperties tinyTokenProperties, Rt rt) {
        this.name = tinyTokenProperties.getName();
        this.timeout = tinyTokenProperties.getTimeout();
        this.rt = rt;
    }

    /**
     * 设置token，token使用UUID，过期时间使用默认值
     *
     * @param id id
     * @return token
     */
    @Override
    public String setToken(Long id) {
        String token = UUID.randomUUID().toString();
        setToken(token, id, timeout);
        return token;
    }

    /**
     * 设置token，token使用UUID
     *
     * @param id      id
     * @param timeout 过期时间(秒)
     * @return token
     */
    @Override
    public String setToken(Long id, long timeout) {
        String token = UUID.randomUUID().toString();
        setToken(token, id, timeout);
        return token;
    }

    /**
     * 设置token，过期时间使用默认值
     *
     * @param token token
     * @param id    id
     */
    @Override
    public void setToken(String token, Long id) {
        setToken(token, id, timeout);
    }

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param timeout 过期时间(秒)
     */
    @Override
    public void setToken(String token, Long id, long timeout) {
        rt.set(name + ":" + id + ":" + token, "", timeout);
    }

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)
     */
    @Override
    public void setToken(String token, Long id, Void extra, long timeout) {
        throw new TinyTokenException("此方法不可用！");
    }

    /**
     * 获取token，通过当前Context，不判断是否有效
     *
     * @return token(不存在返回null)
     */
    @Override
    public String getToken() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(name);
    }

    /**
     * 获取token，通过当前Context，并判断是否有效
     *
     * @return token(不存在或无效返回null)
     */
    @Override
    public String getTokenValid() {
        String token = getToken();
        if (token != null && existByToken(token)) {
            return token;
        }
        return null;
    }

    /**
     * 获取键，通过token
     *
     * @param token token
     * @return 键(不存在返回null)
     */
    private String getKey(String token) {
        Set<String> scan = rt.scan(name + ":*:" + token);
        if (!scan.isEmpty()) {
            return scan.stream().findFirst().get();
        }
        return null;
    }

    /**
     * 获取键列表，通过id
     *
     * @param id id
     * @return 键列表(不存在返回[])
     */
    private Set<String> getKey(Long id) {
        return rt.scan(name + ":" + id + ":*");
    }

    /**
     * 获取token列表，通过id
     *
     * @param id id
     * @return token列表(不存在返回[])
     */
    @Override
    public Set<String> getToken(Long id) {
        Set<String> tokens = new HashSet<>();
        Set<String> keys = getKey(id);
        for (String key : keys) {
            String[] split = key.split(":", -1);
            if (split.length == 3) {
                tokens.add(split[2]);
            }
        }
        return tokens;
    }

    /**
     * 获取id，通过当前Context
     *
     * @return id(不存在返回null)
     */
    @Override
    public Long getId() {
        String token = getToken();
        if (token != null) {
            return getId(token);
        }
        return null;
    }

    /**
     * 获取id，通过token
     *
     * @param token token
     * @return id(不存在返回null)
     */
    @Override
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
     * token是否存在
     *
     * @param token token
     * @return 是否存在
     */
    @Override
    public boolean existByToken(String token) {
        return !rt.scan(name + ":*:" + token).isEmpty();
    }

    /**
     * id是否存在
     *
     * @param id id
     * @return 是否存在
     */
    @Override
    public boolean existById(Long id) {
        return !getKey(id).isEmpty();
    }

    /**
     * 删除，通过token
     *
     * @param token token
     * @return 是否成功
     */
    @Override
    public Boolean deleteByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.delete(key);
        }
        return false;
    }

    /**
     * 删除，通过id
     *
     * @param id id
     * @return 成功个数
     */
    @Override
    public Long deleteById(Long id) {
        return rt.deleteMulti(getKey(id));
    }

    /**
     * 设置过期时间，通过token
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    @Override
    public Boolean expire(String token, long timeout) {
        String key = getKey(token);
        if (key != null) {
            return rt.expire(key, timeout);
        }
        return false;
    }

    /**
     * 设置永不过期，通过token
     *
     * @param token token
     * @return 是否成功
     */
    @Override
    public Boolean persist(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.persist(key);
        }
        return false;
    }

    /**
     * 获取信息，通过token
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    @Override
    public Info<String, Long, Void> getInfoByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            Long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    return new Info<>(token, Long.parseLong(split[1]), expire);
                }
            }
        }
        return null;
    }

    /**
     * 获取信息列表，通过id
     *
     * @param id id
     * @return 信息列表(不存在返回[])
     */
    @Override
    public List<Info<String, Long, Void>> getInfoById(Long id) {
        List<Info<String, Long, Void>> list = new ArrayList<>();
        Set<String> keys = getKey(id);
        for (String key : keys) {
            Long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    list.add(new Info<>(split[2], id, expire));
                }
            }
        }
        return list;
    }

}
