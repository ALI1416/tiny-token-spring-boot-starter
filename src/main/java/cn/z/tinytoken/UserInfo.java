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
     * id
     */
    private static final ThreadLocal<Long> ID = new NamedThreadLocal<>("UserInfo Id");
    /**
     * token
     */
    private static final ThreadLocal<String> TOKEN = new NamedThreadLocal<>("UserInfo Token");
    /**
     * 拓展内容
     */
    private static final ThreadLocal<String> EXTRA = new NamedThreadLocal<>("UserInfo Extra");

    public static Long getId() {
        return ID.get();
    }

    public static void setId(Long id) {
        ID.set(id);
    }

    public static void removeId() {
        ID.remove();
    }

    public static String getToken() {
        return TOKEN.get();
    }

    public static void setToken(String token) {
        TOKEN.set(token);
    }

    public static void removeToken() {
        TOKEN.remove();
    }

    public static String getExtra() {
        return EXTRA.get();
    }

    public static void setExtra(String extra) {
        EXTRA.set(extra);
    }

    public static void removeExtra() {
        EXTRA.remove();
    }

}
