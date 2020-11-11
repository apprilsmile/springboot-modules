package com.appril.delay;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DelayMessageReceiver {

    public  static  final String ORDER_DELAY_QUEUE = "order-delay-queue";

    @RabbitListener(queues = ORDER_DELAY_QUEUE)
    public void getDelayMessage(String odrId, Channel channel, Message message) throws IOException {
        //执行订单超时业务逻辑
        System.out.println("delay-message-receiver: " + odrId);
        //手工ack
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
