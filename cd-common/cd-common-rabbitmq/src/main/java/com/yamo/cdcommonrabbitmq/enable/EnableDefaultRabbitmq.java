package com.yamo.cdcommonrabbitmq.enable;

import com.yamo.cdcommonrabbitmq.config.DirectRabbitConfig;
import com.yamo.cdcommonrabbitmq.config.TopicRabbitConfig;
import com.yamo.cdcommonrabbitmq.config.baseConfig.DefaultRabbitConfiguration;
import com.yamo.cdcommonrabbitmq.service.ConfirmCallbackService;
import com.yamo.cdcommonrabbitmq.service.ReturnCallbackService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 默认rabbitmq启动注解
 * 如果要使用默认的rabbitmq,则需要在启动类上添加该注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({DefaultRabbitConfiguration.class,
        ConfirmCallbackService.class, ReturnCallbackService.class,
        DirectRabbitConfig.class, TopicRabbitConfig.class})
public @interface EnableDefaultRabbitmq {
}
