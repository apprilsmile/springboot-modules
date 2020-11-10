package com.appril.springbootrabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.appril.entity.User;
import com.appril.server.MQSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class SpringbootRabbitmqApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MQSender sender;

    @Test
    void send() throws Exception {
        JSONObject message = new JSONObject();
        message.put("key1","打工人");
        message.put("key2","打工魂");
        message.put("result","打工成为人上人");
        sender.send(message,new HashMap<>());
    }
    @Test
    void sendUser() throws Exception {
        User user = new User();
        user.setUsrNm("admin");
        user.setRlNm("超级管理员");
        user.setRefMp("164984949");
        sender.sendUser(user);
    }
}
