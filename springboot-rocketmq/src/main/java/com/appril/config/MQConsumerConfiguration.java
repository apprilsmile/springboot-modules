package com.appril.config;

import com.appril.listener.ConcurrentlyMessageListenerHandler;
import com.appril.listener.OrderlyMessageListenerHandler;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: appril
 * @Date: 2020/12/23 10:28
 * @Description: mq消费者配置
*/
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rocketmq.consumer")
public class MQConsumerConfiguration {
    public static final Logger LOGGER = LoggerFactory.getLogger(MQConsumerConfiguration.class);

    private String                        namesrvAddr;
    private String                        groupName;
    private int                           consumeThreadMin;
    private int                           consumeThreadMax;
    private String                        topics;
    private int                           consumeMessageBatchMaxSize;

    @Autowired
    private ConcurrentlyMessageListenerHandler concurrentlyMessageListener;

    @Autowired
    private OrderlyMessageListenerHandler orderlyMessageListener;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.consumer", value = "isOnOff", havingValue = "on")
    public DefaultMQPushConsumer defaultMQPushConsumer() throws RuntimeException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(concurrentlyMessageListener);
        //顺序消息时使用
//        consumer.registerMessageListener(orderlyMessageListener);

        // 设置 consumer 第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 设置消费模型，集群还是广播，默认为集群
//        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 设置一次消费消息的条数，默认为 1 条
//        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            // 设置该消费者订阅的主题和tag，如果是订阅该主题下的所有tag，使用*；
            consumer.subscribe(topics, "*");
            // 启动消费
            consumer.start();
            LOGGER.info("consumer is started. groupName:{}, topics:{}, namesrvAddr:{}",groupName,topics,namesrvAddr);

        } catch (Exception e) {
            LOGGER.error("failed to start consumer . groupName:{}, topics:{}, namesrvAddr:{}",groupName,topics,namesrvAddr,e);
            throw new RuntimeException(e);
        }
        return consumer;
    }
}

