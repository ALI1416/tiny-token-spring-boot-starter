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
     * header(默认值"token")
     */
    private String header = "token";
    /**
     * 前缀(默认值"tinytoken")
     */
    private String prefix = "tinytoken";
    /**
     * 过期时间(秒)(默认值2592000[30天])
     */
    private long timeout = 2592000L;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
