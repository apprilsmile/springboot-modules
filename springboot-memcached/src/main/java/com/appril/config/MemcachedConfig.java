package com.appril.config;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @description: memcached配置
 **/
@Configuration
public class MemcachedConfig {

    @Autowired
    private MemcachedProperties memcachedProperties;

    @Bean(name = "memcachedClientBuilder")
    public MemcachedClientBuilder getBuilder() {
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder(memcachedProperties.getServer());

        // 内部采用一致性哈希算法
        memcachedClientBuilder.setSessionLocator(new KetamaMemcachedSessionLocator());
        // 操作的超时时间
        memcachedClientBuilder.setOpTimeout(memcachedProperties.getOpTimeout());
        // 采用二进制传输协议（默认为文本协议）
        memcachedClientBuilder.setCommandFactory(new BinaryCommandFactory());
        // 设置连接池的大小
        memcachedClientBuilder.setConnectionPoolSize(memcachedProperties.getPoolSize());
        // 是否开起失败模式
        memcachedClientBuilder.setFailureMode(memcachedProperties.isFailureMode());
        return memcachedClientBuilder;
    }

    /**
     * 由Builder创建memcachedClient对象，并注入spring容器中
     * @param memcachedClientBuilder
     * @return
     */
    @Bean(name = "memcachedClient")
    public MemcachedClient getClient(@Qualifier("memcachedClientBuilder") MemcachedClientBuilder memcachedClientBuilder) {
        MemcachedClient client = null;
        try {
            client =  memcachedClientBuilder.build();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}