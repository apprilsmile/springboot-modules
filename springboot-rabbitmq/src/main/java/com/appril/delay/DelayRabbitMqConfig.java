package com.appril.delay;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 队列，交换机定义与绑定
 * 延迟队列插件`rabbitmq-delayed-message-exchange`下载地址 https://github.com/rabbitmq/rabbitmq-delayed-message-exchange
 *
 */
@Configuration
public class DelayRabbitMqConfig {

    public  static  final String ORDER_DELAY_QUEUE = "order-delay-queue";
    public  static  final String ORDER_DELAY_EXCHANGE = "order-delay-exchange";
    public  static  final String ORDER_DELAY_BINDING_KEY = "order.delay.*";

    /**
     * 订单队列 - 接收延迟投递的订单
     *
     * 订单队列名称
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder
                .durable(ORDER_DELAY_QUEUE)
                .build();
    }

    /**
     * 订单交换机-延迟交换机 - 消息延迟一定时间之后再投递到绑定的队列
     *
     * 订单延迟交换机
     * @return
     */
    @Bean
    public Exchange orderDelayExchange() {
        Map<String, Object> args = new HashMap<>(1);
        args.put("x-delayed-type", "topic");
        return new CustomExchange(ORDER_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * 订单队列-交换机 绑定
     *
     * @param orderDelayQueue         订单队列
     * @param orderDelayExchange 订单交换机
     * @return
     */
    @Bean
    public Binding orderBinding(Queue orderDelayQueue, Exchange orderDelayExchange) {
        return BindingBuilder
                .bind(orderDelayQueue)
                .to(orderDelayExchange)
                .with(ORDER_DELAY_BINDING_KEY)
                .noargs();
    }
}
