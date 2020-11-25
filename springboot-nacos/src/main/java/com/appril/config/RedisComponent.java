package com.appril.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Redis 配置文件
 *
 * @author: zy
 * @create: 2020-10-27
 **/
@Component
@NacosPropertySource(dataId = "redis.properties",autoRefreshed = true)
@Data
public class RedisComponent {
    @NacosValue(value ="${spring.redis.host}")
    private String host;

    @NacosValue(value ="${spring.redis.port}")
    private int port;

    @NacosValue(value ="${spring.redis.timeout}")
    private int timeout;

    @NacosValue(value ="${spring.redis.pool.max-idle}")
    private int maxIdle;

    @NacosValue(value ="${spring.redis.pool.max-wait}")
    private int maxWaitMillis;

}

