package cn.z.tinytoken.spring;

import cn.z.tinytoken.TinyToken;
import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

/**
 * <h1>轻量级权限认证Spring实现</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * <p>token的类型:String</p>
 * <p>id的类型:Long</p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class TinyTokenForSpring implements TinyToken<String, Long, Void> {

    /**
     * TinyTokenProperties
     */
    private final TinyTokenProperties tinyTokenProperties;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties TinyTokenProperties
     */
    public TinyTokenForSpring(TinyTokenProperties tinyTokenProperties) {
        this.tinyTokenProperties = tinyTokenProperties;
    }

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param timeout 过期时间(秒)<br>
     *                -1:不过期
     */
    @Override
    public void setToken(String token, Long id, int timeout) {

    }

    /**
     * 设置token
     *
     * @param token   token
     * @param id      id
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)<br>
     *                -1:不过期
     */
    @Override
    public void setToken(String token, Long id, Void extra, long timeout) {
    }

    /**
     * 获取token
     *
     * @return token
     */
    @Override
    @Nullable
    public String getToken() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(tinyTokenProperties.getName());
    }

    /**
     * 获取id对应的token列表
     *
     * @param id id
     * @return token列表
     */
    @Override
    public List<String> getToken(Long id) {
        return null;
    }

    /**
     * 获取id
     *
     * @return id
     */
    @Override
    public Long getId() {
        return null;
    }

    /**
     * 获取token对应的id
     *
     * @param token token
     * @return id
     */
    @Override
    public Long getId(String token) {
        return null;
    }

    /**
     * 删除，通过token
     *
     * @param token token
     */
    @Override
    public void deleteForToken(String token) {

    }

    /**
     * 删除，通过id
     *
     * @param id id
     */
    @Override
    public void deleteForId(Long id) {

    }

    /**
     * 获取token对应的信息
     *
     * @param token token
     * @return 信息
     */
    @Override
    public Info<String, Long, Void> getInfoForToken(String token) {
        return null;
    }

    /**
     * 获取id对应的信息列表
     *
     * @param id id
     * @return 信息列表
     */
    @Override
    public List<Info<String, Long, Void>> getInfoForId(Long id) {
        return null;
    }


}
