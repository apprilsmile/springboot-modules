package com.appril.datasource.config;

import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.adaptor.impl.ClickHouseOutsideServiceAdaptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name="spring.datasource.dynamic.type",havingValue = "clickhouse")
public class ClickHouseDataSourceConfig {

    /**
     * clickHouseDataSource
     *
     * @return javax.sql.DataSource
     */
    @Bean(name = "clickHouseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.clickhouse")
    public DataSource clickHouseDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * clickHouseJdbcTemplate
     * @param dataSource 数据源
     * @return JdbcTemplate对象
     */
    @Bean(name = "clickHouseJdbcTemplate")
    public JdbcTemplate clickHouseJdbcTemplate(@Qualifier("clickHouseDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Configuration
    @ConditionalOnProperty(name="spring.datasource.dynamic.type",havingValue = "clickhouse")
    public static class ClickHouseAdaptor {
        @Bean(name = "clickHouseOutsideServiceAdaptor")
        public OutsideServiceAdaptor clickHouseOutsideServiceAdaptor(@Qualifier("clickHouseJdbcTemplate") JdbcTemplate jdbcTemplate) {
            return new ClickHouseOutsideServiceAdaptor(jdbcTemplate);
        }
    }

}

