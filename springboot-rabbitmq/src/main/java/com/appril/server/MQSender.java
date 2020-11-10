package com.appril.server;

import com.appril.constant.RabbitMqConstant;
import com.appril.entity.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback= (correlationData, ack, cause) -> {
        System.out.println("correlationData: " + correlationData);
        System.out.println("ack: " + ack);
        if(!ack){
            System.out.println("异常处理....");
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText, exchange, routingKey) -> System.out.println("return exchange: " + exchange + ", routingKey: "
            + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);

    //发送消息方法调用: 构建Message消息
    public void send(Object message, Map<String, Object> properties) throws Exception {
        MessageProperties mp = new MessageProperties();
        //在生产环境中这里不用Message，而是使用 fastJson 等工具将对象转换为 json 格式发送
        Message msg = new Message(message.toString().getBytes(),mp);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890"+new Date());
        rabbitTemplate.convertAndSend(RabbitMqConstant.MqExchange.SECOND_EXCHANGE.getValue(), RabbitMqConstant.MqRoutingKey.SECOND_ROUTING_KEY.getValue(), msg, correlationData);
    }
    //发送消息方法调用: 构建Message消息
    public void sendUser(User user) throws Exception {
//        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一
        CorrelationData correlationData = new CorrelationData("1234567890"+new Date());
        rabbitTemplate.convertAndSend(RabbitMqConstant.MqExchange.FIRST_EXCHANGE.getValue(), RabbitMqConstant.MqRoutingKey.FIRST_ROUTING_KEY_DO.getValue(), user, correlationData);
    }
}