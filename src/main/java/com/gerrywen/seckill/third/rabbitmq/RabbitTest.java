package com.gerrywen.seckill.third.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerrywen.seckill.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * description:
 * <p>
 * RabbitMQ启动报unknown exchange type 'x-delayed-message'
 * https://blog.csdn.net/weixin_30794491/article/details/96472203
 * <p>
 * docker环境下RabbitMQ实现延迟队列(使用delay插件,非死信队列), 注意版本要和rabbitmq一致
 * https://www.jianshu.com/p/197715cea172
 * <p>
 * Spring Boot整合RabbitMQ详细教程
 * https://blog.csdn.net/yuyeqianhen/article/details/94594711
 * <p>
 * RabbitMQ的三种模式-----直接模式（Direct）
 * https://blog.csdn.net/qq_22596931/article/details/89329024
 * <p>
 * RabbitMQ的三种模式-----分列模式（Fanout）
 * https://blog.csdn.net/qq_22596931/article/details/89329039
 *
 * @author wenguoli
 * @date 2020/3/4 9:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitSendServiceTest rabbitSendServiceTest;

    @Test
    public void sendTest() {

        for (int i = 0; i <10000 ; i++) {
            rabbitSendServiceTest.send(i);
            System.out.println("发送消息"+i);
        }
    }
}
