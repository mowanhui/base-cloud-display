package com.yamo.cdsysmng.consumer;

import cn.hutool.core.bean.BeanUtil;
import com.yamo.cdcommoncore.utils.UUIDUtils;
import com.yamo.cdcommonlog.pojo.LogInfo;
import com.yamo.cdsysmng.domain.entity.SysLog;
import com.yamo.cdsysmng.service.ISysLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 日志消费者
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/14 15:38
 */
@Slf4j
@Component
@RabbitListener(queues = "sys.log")
public class LogConsumer {
    @Autowired
    private ISysLogService iSysLogService;
    @RabbitHandler
    public void processHandler(LogInfo logInfo, Channel channel, Message message) throws IOException {
        try {
            log.info("收到日志消息：{}", logInfo);
            //添加日志
            SysLog sysLog=new SysLog();
            BeanUtil.copyProperties(logInfo,sysLog);
            sysLog.setId(UUIDUtils.getUUID());
            sysLog.setLogId(UUIDUtils.getUUID());
            if(logInfo.getLogType()!=null){
                sysLog.setLogType(logInfo.getLogType().getValue());
            }
            if(logInfo.getOptAction()!=null){
                sysLog.setOptAction(logInfo.getOptAction().getValue());
            }
            if(logInfo.getLogSource()!=null){
                sysLog.setLogSource(logInfo.getLogSource().getValue());
            }
            if(logInfo.getLogCategory()!=null){
                sysLog.setLogCategory(logInfo.getLogCategory().getValue());
            }
            if(logInfo.getCreateTime()==null){
                sysLog.setCreateTime(LocalDateTime.now());
            }
            if(logInfo.getUpdateTime()==null){
                sysLog.setUpdateTime(LocalDateTime.now());
            }
            sysLog.setIsDeleted("0");
            iSysLogService.save(sysLog);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            if (message.getMessageProperties().getRedelivered()) {
                log.error("日志消息已重复处理失败,拒绝再次接收...");
                // 拒绝消息
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.error("日志消息即将再次返回队列处理...");
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        }
    }
}
