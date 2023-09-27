package cn.z.tinytoken.entity;

/**
 * <h1>轻量级权限认证信息拓展</h1>
 *
 * <p>
 * createDate 2023/09/26 14:19:43
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.1.0
 **/
public class TokenInfoExtra extends TokenInfo {

    /**
     * 拓展内容
     */
    private final String extra;

    public TokenInfoExtra() {
        super();
        this.extra = null;
    }

    public TokenInfoExtra(Long id, String token, String extra, long timeout) {
        super(id, token, timeout);
        this.extra = extra;
    }

    public String getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "TokenInfoExtra{" +
                "extra='" + extra + '\'' +
                '}';
    }

}
