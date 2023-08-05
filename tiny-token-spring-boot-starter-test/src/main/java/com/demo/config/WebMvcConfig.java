package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <h1>WebMvc配置类</h1>
 *
 * <p>
 * createDate 2023/03/03 13:13:13
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 全局跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 所有路径
                .allowCredentials(true) // 放行Cookie
                .allowedOriginPatterns("*") // 放行所有原始域
                .allowedMethods("*") // 放行全部请求方式
                .allowedHeaders("*") // 放行全部原始请求头部信息
                .exposedHeaders("*") // 暴露全部原始请求头部信息
        ;
    }

}
