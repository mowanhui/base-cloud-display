package com.yamo.cdcommonrabbitmq.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 *  ConfirmCallback确认模式
 *  消息只要被 rabbitmq broker 接收到就会触发 confirmCallback 回调
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 11:22
 */
@Slf4j
@Component
public class ConfirmCallbackService implements RabbitTemplate.ConfirmCallback{
    /**
     * 手动确认
     * @param correlationData 对象内部只有一个 id 属性，用来表示当前消息的唯一性
     * @param ack 消息投递到broker 的状态，true表示成功
     * @param cause 表示投递失败的原因。
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("ConfirmCallback,消息发送异常,correlationData={} ,ack={}, cause={}", correlationData.getId(), ack, cause);
        } else {
            log.info("ConfirmCallback,发送者已经收到确认，correlationData={} ,ack={}, cause={}", correlationData.getId(), ack, cause);
        }
    }
}
