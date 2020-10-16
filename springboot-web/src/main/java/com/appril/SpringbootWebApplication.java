package com.appril;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.appril.mapper")
public class SpringbootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebApplication.class, args);
    }

}
