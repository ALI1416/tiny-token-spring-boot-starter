package cn.z.tinytoken.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <h1>轻量级权限认证自动配置</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 */
@EnableConfigurationProperties({TinyTokenProperties.class, TinyTokenAuthProperties.class})
public class TinyTokenAutoConfiguration {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(TinyTokenAutoConfiguration.class);

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTokenProperties     TinyTokenProperties
     * @param tinyTokenAuthProperties TinyTokenAuthProperties
     */
    public TinyTokenAutoConfiguration(TinyTokenProperties tinyTokenProperties, TinyTokenAuthProperties tinyTokenAuthProperties) {
        log.info("TinyToken配置：header HEADER {} ，前缀PREFIX {} ，过期时间TIMEOUT {} (秒) ，用户名USERNAME {} ，密码PASSWORD {}",
                tinyTokenProperties.getHeader(), tinyTokenProperties.getPrefix(), tinyTokenProperties.getTimeout(),
                tinyTokenAuthProperties.getUsername(), tinyTokenAuthProperties.getPassword()
        );
    }

}
