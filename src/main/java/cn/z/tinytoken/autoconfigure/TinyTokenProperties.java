package cn.z.tinytoken.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>轻量级权限认证配置属性</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = TinyTokenProperties.TINY_TOKEN_PREFIX)
public class TinyTokenProperties {

    /**
     * 前缀{@value}
     */
    public static final String TINY_TOKEN_PREFIX = "tiny-token";

    /**
     * 名称(默认值"tinytoken")
     */
    private String name;
    /**
     * 过期时间(秒)(默认值2592000[30天])
     */
    private Long timeout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}
