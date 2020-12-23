package com.appril.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author: appril
 * @Date: 2020/12/23 10:28
 * @Description: mq生产者配置
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rocketmq.producer")
public class MQProducerConfiguration {

    public static final Logger LOGGER = LoggerFactory.getLogger(MQProducerConfiguration.class);

    private String             groupName;
    private String             namesrvAddr;
    private Integer            maxMessageSize;
    private Integer            sendMsgTimeout;
    private Integer            retryTimesWhenSendFailed;

    @Bean
    @ConditionalOnProperty(prefix = "rocketmq.producer", value = "isOnOff", havingValue = "on")
    public DefaultMQProducer defaultMQProducer() throws RuntimeException {
        DefaultMQProducer producer = new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        //如果需要同一个 jvm 中不同的 producer 往不同的 mq 集群发送消息，需要设置不同的 instanceName
        //producer.setInstanceName(instanceName);
        //发送消息的最大限制
        producer.setMaxMessageSize(this.maxMessageSize);
        //发送消息超时时间
        producer.setSendMsgTimeout(this.sendMsgTimeout);
        //发送消息失败，设置重试次数，默认为 2 次
        producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        try {
            producer.start();
            LOGGER.info("producer is started. groupName:{}, namesrvAddr: {}", groupName, namesrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("failed to start producer.", e);
            throw new RuntimeException(e);
        }
        return producer;
    }
}
