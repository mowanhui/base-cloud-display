package com.yamo.cdcommonrabbitmq.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 如果消息未能投递到目标 queue 里将触发回调 returnCallback
 * 一旦向 queue 投递消息未成功，这里一般会记录下当前消息的详细投递数据，方便后续做重发或者补偿等操作
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 11:28
 */
@Slf4j
@Component
public class ReturnCallbackService implements RabbitTemplate.ReturnsCallback{

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("returnedMessage ===> replyCode={} ,replyText={} ,exchange={} ,routingKey={}",
                returnedMessage.getReplyCode(), returnedMessage.getReplyText(), returnedMessage.getExchange(), returnedMessage.getRoutingKey());
    }
}
