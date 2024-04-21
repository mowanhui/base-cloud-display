package com.yamo.cdcommonlog.mq;

import cn.hutool.core.lang.UUID;
import com.yamo.cdcommonlog.pojo.LogInfo;
import com.yamo.cdcommonrabbitmq.enums.MqEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 日志mq服务
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/25 23:15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LogMqSender {
    private final RabbitTemplate rabbitTemplate;
    public void sendLogMsg(LogInfo logInfo){
        /**
         * 发送消息
         */
        rabbitTemplate.convertAndSend(MqEnums.SYS_LOG.getExchange(), MqEnums.SYS_LOG.getRoutKey(), logInfo,
                new CorrelationData(UUID.randomUUID().toString()));
    }
}
