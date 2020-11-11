package com.appril.delay;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelayMessageSender {
    public  static  final String ORDER_DELAY_EXCHANGE = "order-delay-exchange";
    public  static  final String ORDER_DELAY_ROUTING_KEY = "order.delay.do";
    public  static  final Integer ORDER_DELAY_TIME = 30000;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelayMessage(String odrId){
        rabbitTemplate.convertAndSend(ORDER_DELAY_EXCHANGE, ORDER_DELAY_ROUTING_KEY, odrId, message ->{
            message.getMessageProperties().setDelay(ORDER_DELAY_TIME);
            return message;
        });
    }

}
