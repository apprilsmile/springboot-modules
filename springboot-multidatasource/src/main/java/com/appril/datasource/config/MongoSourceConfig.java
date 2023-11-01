package com.appril.datasource.config;

import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.adaptor.impl.InternalOutsideServiceAdaptor;
import com.appril.adaptor.impl.MongoOutsideServiceAdaptor;
import com.appril.datasource.utils.SpringContextHolder;
import com.appril.model.internal.service.TraceReportExternalMysqlService;
import com.appril.model.mongo.service.SimpleMongoService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

@Configuration
@ConditionalOnProperty(name="spring.datasource.dynamic.type",havingValue = "mongodb")
public class MongoSourceConfig {


    @Configuration
    @ConditionalOnProperty(name="spring.datasource.dynamic.type",havingValue = "mongodb")
    public static class MongoAdaptor {
        @Bean(name = "mongoOutsideServiceAdaptor")
        public OutsideServiceAdaptor mongoOutsideServiceAdaptor() {
            return new MongoOutsideServiceAdaptor(SpringContextHolder.getBean(SimpleMongoService.class));
        }
    }

}
