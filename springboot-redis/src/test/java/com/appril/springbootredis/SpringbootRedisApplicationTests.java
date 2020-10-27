package com.appril.springbootredis;

import com.appril.util.DistributedLockUtil;
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
        redisUtil.delete("1024");
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
}
