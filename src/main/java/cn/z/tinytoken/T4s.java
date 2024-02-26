package cn.z.tinytoken;

import cn.z.tinytoken.autoconfigure.TinyTokenProperties;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * <h1>轻量级权限认证Spring实现</h1>
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
     * 轻量级权限认证配置属性
     */
    private final TinyTokenProperties tinyTokenProperties;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties TinyTokenProperties
     */
    public T4s(TinyTokenProperties tinyTokenProperties) {
        this.tinyTokenProperties = tinyTokenProperties;
    }

    /**
     * 获取Authorization(当前Context)
     *
     * @return Authorization(不存在返回null)
     * @throws TinyTokenException 不存在Context
     */
    public String getAuth() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new TinyTokenException("不存在Context");
        }
        return getAuth(((ServletRequestAttributes) requestAttributes).getRequest());
    }

    /**
     * 获取Authorization
     *
     * @param request HttpServletRequest
     * @return Authorization(不存在返回null)
     */
    public String getAuth(HttpServletRequest request) {
        return request.getHeader(tinyTokenProperties.getHeader());
    }

    /**
     * Authorization是否正确(当前Context)
     *
     * @return Authorization正确
     * @throws TinyTokenException 不存在Context
     */
    public boolean isCorrect() {
        return isCorrect(getAuth());
    }

    /**
     * Authorization是否正确
     *
     * @param request HttpServletRequest
     * @return Authorization正确
     */
    public boolean isCorrect(HttpServletRequest request) {
        return isCorrect(getAuth(request));
    }

    /**
     * Authorization是否正确
     *
     * @param auth Authorization
     * @return Authorization正确
     */
    public boolean isCorrect(String auth) {
        if (auth == null) {
            return false;
        }
        return decode(auth).equals(tinyTokenProperties.getUsername() + ":" + tinyTokenProperties.getPassword());
    }

    /**
     * 编码
     *
     * @param s 字符串
     * @return 编码的字符串
     */
    public static String encode(String s) {
        return new String(Base64Coder.encode(s.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解码
     *
     * @param s 字符串
     * @return 解码的字符串
     */
    public static String decode(String s) {
        return new String(Base64Coder.decode(s), StandardCharsets.UTF_8);
    }

}
