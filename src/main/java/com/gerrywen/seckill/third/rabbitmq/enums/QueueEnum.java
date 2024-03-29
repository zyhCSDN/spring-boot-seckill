package com.gerrywen.seckill.third.rabbitmq.enums;

import com.gerrywen.seckill.third.rabbitmq.constants.RabbitConsts;
import lombok.Getter;

/**
 * description:
 *
 * @author wenguoli
 * @date 2020/3/4 17:34
 */
@Getter
public enum QueueEnum {

    /**
     * 直接模式1
     */
    DIRECT_MODE_QUEUE_ONE("queue.direct.1", "queue.direct.1", "queue.direct.1"),
    /**
     * 交换机配置
     */
    QUEUE_TWO("queue.2", "queue.2", "queue.2"),
    /**
     * 死信配置
     */
    QUEUE_THREE("queue.3", "queue.3", "queue.3"),

    /**
     * 队列2
     */
    MIAOSHA_MODE_QUEUE("miaosha.1", "miaosha.1", "miaosha.1");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
