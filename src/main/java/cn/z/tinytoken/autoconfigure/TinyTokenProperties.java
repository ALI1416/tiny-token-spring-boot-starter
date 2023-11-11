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
@ConfigurationProperties(prefix = "tiny-token")
public class TinyTokenProperties {

    /**
     * header前缀(默认值"tinytoken")
     */
    private String prefixHeader = "tinytoken";
    /**
     * redis前缀(默认值"tinytoken")
     */
    private String prefixRedis = "tinytoken";
    /**
     * 过期时间(秒)(默认值2592000[30天])
     */
    private long timeout = 2592000L;

    public String getPrefixHeader() {
        return prefixHeader;
    }

    public void setPrefixHeader(String prefixHeader) {
        this.prefixHeader = prefixHeader;
    }

    public String getPrefixRedis() {
        return prefixRedis;
    }

    public void setPrefixRedis(String prefixRedis) {
        this.prefixRedis = prefixRedis;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
