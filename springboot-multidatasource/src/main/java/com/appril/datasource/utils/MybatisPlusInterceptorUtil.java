package com.appril.datasource.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Mybatis-Plus分页配置工具类
 */

@Slf4j
public class MybatisPlusInterceptorUtil {
    /**
     * mybatisPlusInterceptor
     *
     * @param dataSource
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     */
    public static MybatisPlusInterceptor mybatisPlusInterceptor(DataSource dataSource)
                                                                                      throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String dbName = connection.getMetaData().getDatabaseProductName();
            DbType dbType;
            switch (dbName) {
                case "Oracle":
                    dbType = DbType.ORACLE;
                    break;
                case "MySQL":
                    dbType = DbType.MYSQL;
                    break;
                default:
                    String message = "数据库" + dbName + "方言不适配！需要适配后重试！";
                    log.error(message);
                    throw new RuntimeException(message);
            }
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
            return interceptor;
        } catch (Exception e) {
            throw e;
        }
    }
}
