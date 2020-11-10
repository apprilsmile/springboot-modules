package com.appril.server;

import com.appril.constant.RabbitMqConstant;
import com.appril.entity.User;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class MQReceiver {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.SECOND_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConstant.SECOND_EXCHANGE, type = "direct",  ignoreDeclarationExceptions = "true"),
                    key = RabbitMqConstant.SECOND_BINDING_KEY
            )
    )
    public void onMessage(Message message, Channel channel) throws IOException {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int i = 10/0;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //手工ack
        channel.basicAck(deliveryTag,true);
        System.out.println("receive--1: " + new String(message.getBody()));
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitMqConstant.FIRST_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitMqConstant.FIRST_EXCHANGE, type = "topic", ignoreDeclarationExceptions = "true"),
                    key = RabbitMqConstant.FIRST_BINDING_KEY
            )
    )
    public void onUserMessage(@Payload User user, Channel channel, @Headers Map<String,Object> headers) throws IOException {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //手工ack
        channel.basicAck(deliveryTag,true);
        System.out.println("receive--user: " + user.toString());
    }
}
