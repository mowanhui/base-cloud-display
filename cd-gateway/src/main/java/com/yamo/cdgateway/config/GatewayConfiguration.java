package com.yamo.cdgateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamo.cdgateway.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置类
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/25 15:30
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }
}
