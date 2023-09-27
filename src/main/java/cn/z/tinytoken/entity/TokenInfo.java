package cn.z.tinytoken.entity;

/**
 * <h1>轻量级权限认证信息</h1>
 *
 * <p>
 * createDate 2023/09/26 14:18:02
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.1.0
 **/
public class TokenInfo {

    /**
     * id
     */
    private final Long id;
    /**
     * token
     */
    private final String token;
    /**
     * 过期时间(秒)<br>
     * -1:不过期
     */
    private final long timeout;

    public TokenInfo() {
        this.token = null;
        this.id = null;
        this.timeout = -1;
    }

    public TokenInfo(Long id, String token, long timeout) {
        this.token = token;
        this.id = id;
        this.timeout = timeout;
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public long getTimeout() {
        return timeout;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", timeout=" + timeout +
                '}';
    }

}
