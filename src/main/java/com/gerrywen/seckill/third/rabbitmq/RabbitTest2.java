//package com.gerrywen.seckill.third.rabbitmq;
//
//import cn.hutool.core.date.DateUtil;
//import com.gerrywen.seckill.third.rabbitmq.constants.RabbitConsts;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
///**
// * description:
// *
// * RabbitMQ启动报unknown exchange type 'x-delayed-message'
// * https:blog.csdn.net/weixin_30794491/article/details/96472203
// *
// * docker环境下RabbitMQ实现延迟队列(使用delay插件,非死信队列), 注意版本要和rabbitmq一致
// * https:www.jianshu.com/p/197715cea172
// *
// * Spring Boot整合RabbitMQ详细教程
// * https:blog.csdn.net/yuyeqianhen/article/details/94594711
// *
// * RabbitMQ的三种模式-----直接模式（Direct）
// * https:blog.csdn.net/qq_22596931/article/details/89329024
// *
// * RabbitMQ的三种模式-----分列模式（Fanout）
// * https:blog.csdn.net/qq_22596931/article/details/89329039
// *
// * @author:ZHAOYONGHENG
// * @date:2023/2/27
// * @version:1.0.0
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class RabbitTest2 {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//
//    /**
//     * 测试直接模式发送
//     * DirectQueueOneHandler 接收到信息
//     */
//    @Test
//    public void sendDirect() {
//        rabbitTemplate.convertAndSend(RabbitConsts.DIRECT_MODE_QUEUE_ONE, ("direct message"));
//    }
//
//    /**
//     * 测试分列模式发送
//     * DirectQueueOneHandler 和 QueueTwoHandler 接收到信息
//     */
//    @Test
//    public void sendFanout() {
//        rabbitTemplate.convertAndSend(RabbitConsts.FANOUT_MODE_QUEUE, "", ("fanout message"));
//    }
//
//    /**
//     * 测试主题模式发送1
//     *
//     * DirectQueueOneHandler 和 QueueTwoHandler 接收到信息
//     *
//     * routingKey: queue.aaa.bbb
//     * topicBinding: queue.# 和 *.queue
//     *
//     * 符号 # 匹配一个或多个词，符号 * 匹配不多不少一个词。 例如 usa.# 能够匹配到 usa.news.XXX ，但是 usa.* 只会匹配到 usa.XXX 。
//     */
//    @Test
//    public void sendTopic1() {
//        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "queue.aaa.bbb", ("topic message"));
//    }
//
//    /**
//     * 测试主题模式发送2
//     *
//     * QueueTwoHandler 接收到信息
//     *
//     * routingKey: ccc.queue
//     * topicBinding: *.queue
//     */
//    @Test
//    public void sendTopic2() {
//        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "ccc.queue", ("topic message"));
//    }
//
//    /**
//     * 测试主题模式发送3
//     *
//     * QueueTwoHandler 和 QueueThreeHandler接收到信息
//     *
//     *
//     * routingKey: 3.queue
//     * topicBinding: *.queue 和 3.queue
//     */
//    @Test
//    public void sendTopic3() {
//        rabbitTemplate.convertAndSend(RabbitConsts.TOPIC_MODE_QUEUE, "3.queue", ("topic message"));
//    }
//
//    /**
//     * 测试延迟队列发送
//     */
//    @Test
//    public void sendDelay() {
//        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_EXCHANGE, RabbitConsts.DELAY_QUEUE, ("delay message, delay 5s, " + DateUtil
//                .date()), message -> {
//            message.getMessageProperties().setHeader("x-delay", 5000);
//            return message;
//        });
//        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_EXCHANGE, RabbitConsts.DELAY_QUEUE, ("delay message,  delay 2s, " + DateUtil
//                .date()), message -> {
//            message.getMessageProperties().setHeader("x-delay", 2000);
//            return message;
//        });
//        rabbitTemplate.convertAndSend(RabbitConsts.DELAY_EXCHANGE, RabbitConsts.DELAY_QUEUE, ("delay message,  delay 8s, " + DateUtil
//                .date()), message -> {
//            message.getMessageProperties().setHeader("x-delay", 8000);
//            return message;
//        });
//    }
//
//}
