package com.yamo.cdcommonapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2024/4/13 16:43
 */
@Configuration
@EnableFeignClients(basePackages = {"com.yamo.**.feign"})
public class FeignEnableConfig {
}
