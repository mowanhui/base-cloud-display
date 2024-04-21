package com.yamo.cdcommonrabbitmq.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
//    @Bean
//    public Queue aisLocationLayerQueue(){
//        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
//        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//        // autoDelete:是否自动删除，有消息者订阅本队列，然后所有消费者都解除订阅此队列，会自动删除。
//        // arguments：队列携带的参数，比如设置队列的死信队列，消息的过期时间等等。
//        return new Queue(MqEnums.SYS_LOG.getQueue(),true);
//    }
//
//    @Bean
//    TopicExchange aisLocationExchange(){
//        // durable:是否持久化,默认是false,持久化交换机。
//        // autoDelete:是否自动删除，交换机先有队列或者其他交换机绑定的时候，然后当该交换机没有队列或其他交换机绑定的时候，会自动删除。
//        // arguments：交换机设置的参数，比如设置交换机的备用交换机（Alternate Exchange），当消息不能被路由到该交换机绑定的队列上时，会自动路由到备用交换机
//        return new TopicExchange(MqEnums.SYS_LOG.getExchange(),true,false);
//    }
//
//    //绑定交换机和队列
//    @Bean
//    Binding BindingAisLocationLayer(){
//        //bind队列to交换机中with路由key（routing key）
//        return BindingBuilder.bind(aisLocationLayerQueue()).to(aisLocationExchange()).with(MqEnums.SYS_LOG.getRoutKey());
//    }
}
