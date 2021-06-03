package com.appril.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     *  3.4.0之后新配置
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
//        paginationInterceptor.setMaxLimit(200L);//设置默认单次查询最大记录数
        paginationInterceptor.setDbType(DbType.MYSQL);//设置数据库类型
        paginationInterceptor.setOverflow(false);//设置超过总页数后是否返回最后一页数据
        mybatisPlusInterceptor.addInnerInterceptor(paginationInterceptor);//将插件放入MybatisPlusInterceptor中才能生效
        return mybatisPlusInterceptor;
    }
}