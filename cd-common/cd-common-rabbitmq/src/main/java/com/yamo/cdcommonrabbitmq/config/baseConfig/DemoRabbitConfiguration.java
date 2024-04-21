package com.yamo.cdcommonrabbitmq.config.baseConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用于多源rabbitmq的一个demo
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rabbitmq.demo1")
public class DemoRabbitConfiguration extends BaseRabbitProperties {
    @Bean(name = "DemoConnectionFactory")
    public ConnectionFactory demoConnectionFactory() {
        return this.getConnectionFactory();
    }
    @Bean(name = "DemoListenerFactory")
    public SimpleRabbitListenerContainerFactory demoListenerFactory(@Qualifier("DemoConnectionFactory") ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        // 并发消费者数量
        containerFactory.setConcurrentConsumers(1);
        containerFactory.setMaxConcurrentConsumers(20);
        // 预加载消息数量 -- QOS
        containerFactory.setPrefetchCount(5);
        //消息序列化方式
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        return containerFactory;
    }

    @Bean(name = "DemoRabbitTemplate")
    public RabbitTemplate demoRabbitTemplate( @Qualifier("DemoConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
