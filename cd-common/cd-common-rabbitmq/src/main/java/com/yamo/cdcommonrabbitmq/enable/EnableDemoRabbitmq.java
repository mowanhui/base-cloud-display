package com.yamo.cdcommonrabbitmq.enable;

import com.yamo.cdcommonrabbitmq.config.baseConfig.DemoRabbitConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动demo的rabbitmq
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DemoRabbitConfiguration.class})
public @interface EnableDemoRabbitmq {
}
