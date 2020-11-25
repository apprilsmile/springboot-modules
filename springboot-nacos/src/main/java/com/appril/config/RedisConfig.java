package com.appril.config;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * Redis 配置文件
 *
 * @author: zy
 * @create: 2020-10-27
 * 字段需完全一致，无法定制化
 **/
@NacosConfigurationProperties(prefix = "spring.redis", dataId = "redis.properties", type = ConfigType.PROPERTIES, autoRefreshed = true)
@Configuration
@Data
public class RedisConfig {

    private String host;

    private int port;

    private int timeout;


}

