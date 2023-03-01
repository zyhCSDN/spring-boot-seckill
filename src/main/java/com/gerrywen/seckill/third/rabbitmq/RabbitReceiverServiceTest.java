package com.gerrywen.seckill.third.rabbitmq;

import com.gerrywen.seckill.third.rabbitmq.enums.QueueEnum;
import com.gerrywen.seckill.third.rabbitmq.listen.AbstractMessageHandler;
import com.gerrywen.seckill.third.rabbitmq.receiver.AbstractReceiverHandler;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 *
 * 监听普通队列
 * description:
 *
 * @author wenguoli
 * @date 2020/3/5 9:15
 */
@Service
public class RabbitReceiverServiceTest extends AbstractReceiverHandler<String> {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitSendServiceTest rabbitSendServiceTest;


    @PostConstruct
    public void init() {
        this.routingKey(QueueEnum.DIRECT_MODE_QUEUE_ONE.getRouteKey())
                .queue(QueueEnum.DIRECT_MODE_QUEUE_ONE.getName())
                .exchange(QueueEnum.DIRECT_MODE_QUEUE_ONE.getExchange())
                .registerQueue();
    }

    @Override
    public AbstractMessageHandler<String> messageHandler() {
        return new AbstractMessageHandler<String>() {
        //第四步
            @Override
            public boolean handleMessage(String message, Channel channel) {
                logger.info("Test 接收消息：{}", message);
                //未付款
                if ((Integer.valueOf(message)%2)==0){
                    logger.info("message=============：{}", message);
                    rabbitSendServiceTest.send(QueueEnum.QUEUE_THREE.getExchange(),
                            QueueEnum.QUEUE_THREE.getRouteKey(), QueueEnum.QUEUE_TWO.getName(),QueueEnum.QUEUE_TWO.getExchange(),QueueEnum.QUEUE_TWO.getRouteKey(),"send deal-test message"+message,120000L);
                }
                return true;
            }
        };
    }
}
