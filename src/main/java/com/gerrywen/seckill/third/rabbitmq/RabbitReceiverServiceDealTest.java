package com.gerrywen.seckill.third.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.listen.AbstractMessageHandler;
import com.gerrywen.seckill.third.rabbitmq.message.MqMessage;
import com.gerrywen.seckill.third.rabbitmq.receiver.AbstractReceiverHandler;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 *
 * 监听死信队列
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:15
 */
public class RabbitReceiverServiceDealTest {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue.3"),
            exchange = @Exchange(value = "queue.2"),
            key = {"queue.2"}
    ))
    public Boolean booleanoNMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        MqMessage mqMessage = JSON.parseObject(message.getBody(), MqMessage.class);
        logger.error("超时未支付,接收死信消息：{}", JSONArray.toJSONString(mqMessage.getMessageBody()));
        channel.basicAck(deliveryTag,false);
        return true;
    }
}
