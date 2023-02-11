package com.appril.config;

import com.appril.listener.MessageSubListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis消息队列配置
 *
 * @author zy
 * @date 2023-02-11
 **/
@Configuration
public class RedisMessageListenerConfig{

    private static final String SUB_KEY = "message:pool";

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(SUB_KEY));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageSubListener redisPubSubListener){
        return new MessageListenerAdapter(redisPubSubListener, "onMessage");
    }

}



