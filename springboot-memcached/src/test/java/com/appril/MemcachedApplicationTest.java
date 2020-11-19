package com.appril;

import com.appril.util.MemcachedUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeoutException;

@SpringBootTest
class MemcachedApplicationTest {

    @Autowired
    private MemcachedUtil memcachedUtil;
    @Test
    void contextLoads() throws InterruptedException, MemcachedException, TimeoutException {
//        memcachedUtil.set("memcachedTest","早安打工人",0);
//        memcachedUtil.prepend("memcachedTest","打工人，打工魂，早起打工人上人，");
        System.out.println(memcachedUtil.get("memcachedTest"));
    }


}
