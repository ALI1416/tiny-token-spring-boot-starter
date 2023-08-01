package cn.z.tinytoken;

import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * <h1>轻量级权限认证Spring实现</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * <p>token的类型:String</p>
 * <p>id的类型:Long</p>
 * <p>没有拓展内容</p>
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
        rt.set(name + ":" + id + ":" + token, name, timeout);
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
    public String setToken(Long id, int timeout) {
        String token = UUID.randomUUID().toString();
        rt.set(name + ":" + id + ":" + token, name, timeout);
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
        rt.set(name + ":" + id + ":" + token, name, timeout);
    }

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param timeout 过期时间(秒)
     */
    @Override
    public void setToken(String token, Long id, int timeout) {
        rt.set(name + ":" + id + ":" + token, name, timeout);
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
        throw new TinyTokenException("没有实现此方法！");
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
        if (token != null) {
            Set<String> scan = rt.scan(name + ":*:" + token);
            if (!scan.isEmpty()) {
                return token;
            }
        }
        return null;
    }

    /**
     * 获取token列表，通过id
     *
     * @param id id(不存在返回[])
     * @return token列表
     */
    @Override
    public Set<String> getToken(Long id) {
        return rt.scan(name + ":" + id + ":*");
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
        Set<String> scan = rt.scan(name + ":*:" + token);
        if (!scan.isEmpty()) {
            String[] split = scan.stream().findFirst().get().split(":", -1);
            if (split.length == 3) {
                return Long.parseLong(split[1]);
            }
        }
        return null;
    }

    /**
     * 获取过期时间，通过当前Context
     *
     * @return 过期时间(秒)(不存在返回null ， 不过期返回 - 1)
     */
    @Override
    public Long getTimeoutByToken() {
        String token = getToken();
        if (token != null) {
            return getTimeoutByToken(token);
        }
        return null;
    }

    /**
     * 获取过期时间，通过token
     *
     * @param token token
     * @return 过期时间(秒)(不存在返回null ， 不过期返回 - 1)
     */
    @Override
    public Long getTimeoutByToken(String token) {
        Long expire = rt.getExpire(token);
        if (expire == -2) {
            return null;
        }
        return expire;
    }

    /**
     * 获取过期时间，通过token
     *
     * @param id id
     * @return 过期时间(秒)列表(不存在返回[] ， 不过期返回 - 1)
     */
    @Override
    public List<Long> getTimeoutById(Long id) {
        return null;
    }

    /**
     * token是否存在
     *
     * @param token token
     */
    @Override
    public boolean existByToken(String token) {
        return false;
    }

    /**
     * id是否存在
     *
     * @param id id
     */
    @Override
    public boolean existById(Long id) {
        return false;
    }

    /**
     * 删除，通过token
     *
     * @param token token
     */
    @Override
    public void deleteByToken(String token) {

    }

    /**
     * 删除，通过id
     *
     * @param id id
     */
    @Override
    public void deleteById(Long id) {

    }

    /**
     * 获取信息，通过token
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    @Override
    public Info<String, Long, Void> getInfoByToken(String token) {
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
        return null;
    }

}
