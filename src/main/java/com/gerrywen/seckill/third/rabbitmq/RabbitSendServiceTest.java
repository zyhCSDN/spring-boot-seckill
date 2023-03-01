package com.gerrywen.seckill.third.rabbitmq;

import com.gerrywen.seckill.third.rabbitmq.constants.RabbitConsts;
import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.sender.AbstractSendService;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:15
 */
@Service
public class RabbitSendServiceTest extends AbstractSendService {


    /**
     * 测试数据时，需要把测试类和主启动来都启起来
     */
    public void send(int i) {
        //第一步 发送普通消息(类似下订单)
        this.send(QueueEnum.DIRECT_MODE_QUEUE_ONE.getExchange(),
                QueueEnum.DIRECT_MODE_QUEUE_ONE.getRouteKey(), QueueEnum.DIRECT_MODE_QUEUE_ONE.getName(),i);
    }

    @Override
    public void handleConfirmCallback(String messageId, boolean ack, String cause) {
        if (!ack) {
            logger.info("打印异常处理....");
        }
        logger.info("messageId:" + messageId + "---ack:" + ack + "----cause:" + cause);
    }

    @Override
    public void handleReturnCallback(Message message, int replyCode, String replyText, String routingKey) {
        logger.info("消息丢失message: {}, replyCode: {}, replyText: {}, routingKey: {}, ", message, replyCode, replyText, routingKey);
    }
}
