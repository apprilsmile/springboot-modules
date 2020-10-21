package com.appril;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@SpringBootApplication
@MapperScan("com.appril.mapper")
public class SpringbootWebApplication extends WebMvcConfigurationSupport {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebApplication.class, args);
    }

     @Value("${uploads.uploadHandlerPath}")
     private String  uploadHandlerPath ;

     @Value("${uploads.uploadResourceLocation}")
     private String  uploadResourceLocation ;

    /**
     *  配置url 映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadHandlerPath).addResourceLocations("file:" + uploadResourceLocation);
        super.addResourceHandlers(registry);
    }
}
