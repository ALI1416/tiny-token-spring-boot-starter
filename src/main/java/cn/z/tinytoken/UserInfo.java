package cn.z.tinytoken;

import org.springframework.core.NamedThreadLocal;

/**
 * <h1>用户信息</h1>
 *
 * <p>
 * createDate 2023/12/19 10:04:24
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.4.1
 **/
public class UserInfo {

    private UserInfo() {
    }

    /**
     * token
     */
    private static final ThreadLocal<String> TOKEN = new NamedThreadLocal<>("UserInfo Token");

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    public static void removeToken() {
        TOKEN.remove();
    }

}
