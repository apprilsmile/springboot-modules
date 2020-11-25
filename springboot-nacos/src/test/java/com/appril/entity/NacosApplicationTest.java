package com.appril.entity;

import com.alibaba.fastjson.JSONObject;
import com.appril.config.RedisConfig;
import com.appril.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class NacosApplicationTest {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Test
    void contextLoads() {
        List<SysUser> sysUsers = sysUserMapper.selectList(new QueryWrapper<>());
        sysUsers.stream().forEach(e-> System.out.println(JSONObject.toJSONString(e)));
    }

    @Autowired
    private RedisConfig config;

    @Test
    void configTest(){
        System.out.println(config.getHost());
    }
}
