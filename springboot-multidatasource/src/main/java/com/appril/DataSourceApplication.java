package com.appril;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author zhangyang
 * @date 2023/10/31 11:42
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages = "com.appril")
public class DataSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataSourceApplication.class, args);
    }
}