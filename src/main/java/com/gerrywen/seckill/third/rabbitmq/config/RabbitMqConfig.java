package com.gerrywen.seckill.third.rabbitmq.config;

import com.gerrywen.seckill.third.rabbitmq.constants.RabbitConsts;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RabbitMQ配置，主要是配置队列，如果提前存在该队列，可以省略本配置类
 * </p>
 * <p>
 * <p>
 * description: RabbitMQ配置，主要是配置队列，如果提前存在该队列，可以省略本配置类
 *
 * @author wenguoli
 * @date 2020/3/4 9:51
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 注册rabbitMQ的Connection
     * <p>
     * ConfigurableBeanFactory.SCOPE_PROTOTYPE:
     * 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置
     * 主要是为了设置回调类
     *
     * @param connectionFactory 工厂类
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        // 如果消息要设置成回调，则以下的配置必须要设置成true   如果你们在配置确认回调，测试发现无法触发回调函数，那么存在原因也许是因为版本导致的配置项不起效，
        //可以把publisher-confirms: true 替换为  publisher-confirm-type: correlated
        // 确认消息投递到交换机 若使用confirm-callback ，必须要配置publisherConfirms 为true
        connectionFactory.setPublisherConfirms(true);
        // 确认消息到队列 若使用return-callback，必须要配置publisherReturns为true
        connectionFactory.setPublisherReturns(true);
        //rabbit模板
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->{
//            if (ack) {
//                log.info("消息成功发送到Exchange，入队列消费成功回调:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
//            } else {
//                log.info("消息失败发送到Exchange，入队列消费失败回调:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
//            }
//        });
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey)
//                -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }

    /**
     * 注册rabbitAdmin 方便管理
     *
     * @param connectionFactory 工厂类
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    /**
     *
     *
     * 首先，消息经过第一个中转站，即基本消息模型中的基本交换机，由基本交换机和基本路由绑定是死信队列，等死信队列的TTL到了，
     * 这个的TTL消息时可以设置的，死信队列也可以设置，两种取最小的。TTL的倒计时到了就消息就会被转发到死信交换机，
     * 死信交换机跟死信路由绑定的是真正的队列，队列的消费者受到消息后，处理逻辑。这段一定要看明白
     * 死信交换机这么用，
     * https://blog.csdn.net/weixin_42324034/article/details/108425605?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522161732563516780255216778%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=161732563516780255216778&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_v2~rank_v29-2-108425605.first_rank_v2_pc_rank_v29&utm_term=rabbit+mq+%E4%B8%8B%E5%8D%95%E4%BF%9D%E5%AD%98%E6%95%B0%E6%8D%AE%E5%BA%93%E4%B9%8B%E5%90%8E%EF%BC%8C%E5%8F%91%E9%80%81%E6%AD%BB%E4%BF%A1%E9%98%9F%E5%88%97%E6%B6%88%E6%81%AF
     * 死信队列消息模型构建----------------------------------------------------------------------------------**/

//    // 创建第一个中转站
//    //创建死信队列
//    @Bean(name = "basicDeadQueue")
//    public Queue basicDeadQueue() {
//        Map<String, Object> params = new HashMap<>();
//        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
//        params.put("x-dead-letter-exchange", "basicDeadExchange");
//        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
//        params.put("x-dead-letter-routing-key", "deal-key");
//        // 注意这里是毫秒单位，这里我们给10秒
//        params.put("x-message-ttl",  30000);
//        return new Queue("basicDeadQueue", true, false, false, params);
//    }
//
//    //创建“基本消息模型”的基本交换机，面向生产者
//    @Bean
//    public TopicExchange basicProducerExchange() {
//        //创建并返回基本交换机实例
//        return new TopicExchange("basicNormalQueue", true, false);
//    }
//    //创建“基本消息模型”的基本绑定（基本交换机+基本路由），面向生产者
//    @Bean
//    public Binding basicProducerBinding() {
//        //创建并返回基本消息模型中的基本绑定(注意这里是正常交换机跟死信队列绑定在一定，不叫死信路由)
//        return BindingBuilder.bind(basicDeadQueue()).to(basicProducerExchange()).with("normal-deal-key");
//    }
//

//    // 创建第二个中转站
//    // 创建真正队列，面向消费者
//    @Bean(name = "realConsumerQueue")
//    public Queue realConsumerQueue() {
//        //创建并返回面向消费者的真正队列实例
//        return new Queue("realConsumerQueue", true);
//    }
//    // 创建死信交换机
//    @Bean
//    public TopicExchange basicDeadExchange() {
//        //创建并返回死信交换机实例
//        return new TopicExchange("basicDeadExchange", true, false);
//    }
//    // 创建死信路由及其绑定
//    @Bean
//    public Binding basicDeadBinding() {
//        //创建死信路由及其绑定实例
//        return BindingBuilder.bind(realConsumerQueue()).to(basicDeadExchange()).with("deal-key");
//    }


}
