package com.appril.listener;

import com.alibaba.fastjson.JSON;
import com.appril.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 集群消费，多个消费者同时消费消息
 * @author zhangyang
 * @date 2023/2/11 14:37
 */
@Component
@Slf4j
public class MessageSubListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String pattern = new String(bytes);
        SysUser sysUser = JSON.parseObject(message.getBody(), SysUser.class);
        log.info("message.body:{},pattern:{}",JSON.toJSONString(sysUser),pattern);
    }
}
