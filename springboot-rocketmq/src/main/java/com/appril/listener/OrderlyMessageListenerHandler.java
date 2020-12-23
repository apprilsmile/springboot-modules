package com.appril.listener;

import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class OrderlyMessageListenerHandler implements MessageListenerOrderly {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderlyMessageListenerHandler.class);
    private static String TOPIC = "TestTopic";
    /**
     * 默认msg里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
     * 不要抛异常，如果没有return CONSUME_SUCCESS ，consumer会重新消费该消息，直到return CONSUME_SUCCESS
     * @return
     */
    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext consumeOrderlyContext) {
        if (CollectionUtils.isEmpty(msgs)) {
            LOGGER.info("receive blank msg...");
            return ConsumeOrderlyStatus.SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        String msg = new String(messageExt.getBody());
        if (messageExt.getTopic().equals(TOPIC)) {
            // mock 消费逻辑
            mockConsume(msg);
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private void mockConsume(String msg){
        LOGGER.info("receive msg: {}.", msg);
    }
}


