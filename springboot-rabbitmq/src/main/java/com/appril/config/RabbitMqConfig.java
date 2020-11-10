package com.appril.config;

import com.appril.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {



    @Autowired
    private QueueConfig queueConfig;
    @Autowired
    private ExchangeConfig exchangeConfig;

    /**
     * 连接工厂
     */
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     将消息队列1和交换机进行绑定
     */
    @Bean
    public Binding binding_first() {
        return BindingBuilder.bind(queueConfig.firstQueue()).to(exchangeConfig.topicExchange()).with(RabbitMqConstant.MqBindingKey.FIRST_BINDING_KEY.getValue());
    }

    /**
     * 将消息队列2和交换机进行绑定
     */
    @Bean
    public Binding binding_second() {
        return BindingBuilder.bind(queueConfig.secondQueue()).to(exchangeConfig.directExchange()).with(RabbitMqConstant.MqBindingKey.SECOND_BINDING_KEY.getValue());
    }



}
