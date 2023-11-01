package com.appril;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author zhangyang
 * @date 2023/10/31 11:42
 */
@EnableMongoRepositories(basePackages = { "com.appril.model.mongo.repository" })
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages = "com.appril")
public class DataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class, args);
    }
}