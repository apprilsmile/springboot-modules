package com.appril.datasource.config;

import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.adaptor.impl.MongoOutsideServiceAdaptor;
import com.appril.model.mongo.service.SimpleMongoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.dynamic.type", havingValue = "mongodb")
public class MongoSourceConfig {


    @Configuration
    public class MongoAdaptor {
        @Bean(name = "mongoOutsideServiceAdaptor")
        public OutsideServiceAdaptor mongoOutsideServiceAdaptor(SimpleMongoService simpleMongoService) {
            return new MongoOutsideServiceAdaptor(simpleMongoService);
        }
    }

}
