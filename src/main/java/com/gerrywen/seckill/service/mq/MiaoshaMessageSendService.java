package com.gerrywen.seckill.service.mq;

import com.alibaba.fastjson.JSONArray;
import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.sender.AbstractSendService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

/**
 * program: spring-boot-seckill->MiaoshaMessageMqService
 * description:
 * author: gerry
 * created: 2020-03-07 11:17
 **/
@Service
public class MiaoshaMessageSendService extends AbstractSendService {

    public void send(MiaoshaMessage miaoshaMessage) {
        this.send(QueueEnum.MIAOSHA_MODE_QUEUE.getExchange(),
                QueueEnum.MIAOSHA_MODE_QUEUE.getRouteKey(), QueueEnum.MIAOSHA_MODE_QUEUE.getName(), miaoshaMessage);
    }

    @Override
    public void handleConfirmCallback(String messageId, boolean ack, String cause) {
        if (ack) {
            logger.info("进入 handleConfirmCallback方法 消息确认消费。。。");
            logger.info("messageId:{},ack:{},cause:{}",messageId,true,cause);
        }else {
            logger.error("打印异常处理....");
        }
    }

    @Override
    public void handleReturnCallback(Message message, int replyCode, String replyText, String routingKey) {

        logger.info("message:{},replyCode:{},replyText:{},routingKey:{}", JSONArray.toJSONString(message),replyCode,replyText,routingKey);

    }
}
