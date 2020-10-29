package com.appril.springbootredis;

import com.alibaba.fastjson.JSONObject;
import com.appril.entity.SysUser;
import com.appril.util.DistributedLockUtil;
import com.appril.util.RedisTemplateUtil;
import com.appril.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRedisApplicationTests {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DistributedLockUtil lockUtil;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Test
    void stringGet() {
        System.out.println(redisUtil.get("1024"));
    }

    @Test
    void stringSet() {
        redisUtil.set("1024", "加油打工人！");
    }

    @Test
    void stringDelete() {
        redisUtil.delete("2048");
    }

    @Test
    void tryGetDistributedLock1() {
        boolean flag = lockUtil.tryGetDistributedLock("1024", "1024", 30000);
        String message = "获取分布式锁成功！";
        if (!flag) {
            message = "很遗憾，获取分布式锁失败！";
        }
        System.out.println(message);
        System.out.println(redisUtil.get("1024"));
    }

    @Test
    void tryGetDistributedLock2() {
        boolean flag = lockUtil.tryGetDistributedLock("1024", "2048", 30000);
        String message = "获取分布式锁成功！";
        if (!flag) {
            message = "很遗憾，获取分布式锁失败！";
        }
        System.out.println(message);
        System.out.println(redisUtil.get("1024"));
    }

    @Test
    void releaseDistributedLock1() {
        boolean flag = lockUtil.releaseDistributedLock("1024", "1024");
        String message = "释放分布式锁成功！";
        if (!flag) {
            message = "很遗憾，释放分布式锁失败！";
        }
        System.out.println(message);
        System.out.println(redisUtil.get("1024"));
    }

    @Test
    void releaseDistributedLock2() {
        boolean flag = lockUtil.releaseDistributedLock("1024", "2048");
        String message = "释放分布式锁成功！";
        if (!flag) {
            message = "很遗憾，释放分布式锁失败！";
        }
        System.out.println(message);
        System.out.println(redisUtil.get("1024"));
    }

    @Test
    void templateGet() {
        System.out.println(redisTemplateUtil.get("2048"));
    }

    @Test
    void templateSet() {
        SysUser user = new SysUser();
        user.setId(123456L);
        user.setUsrNm("打工人");
        user.setIsDel(0);
        user.setEMail("123566@163.com");
        user.setRlNm("人人都是打工人");
        redisTemplateUtil.set("2048", JSONObject.toJSONString(user));
    }

    @Test
    void templateDelete() {
        redisTemplateUtil.del("1024");
    }
}
