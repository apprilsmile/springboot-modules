package com.appril.datasource.config;

import com.appril.adaptor.OutsideServiceAdaptor;
import com.appril.adaptor.impl.InternalOutsideServiceAdaptor;
import com.appril.model.internal.service.TraceReportExternalMysqlService;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.dynamic.type", havingValue = "internal")
@MapperScan(basePackages = "com.appril.model.internal.mapper", sqlSessionFactoryRef = "internalSessionFactory")
public class InternalDataSourceConfig {

    /**
     * internalDataSource
     *
     * @return javax.sql.DataSource
     */
    @Bean(name = "internalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.internal")
    public DataSource internalDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * internalJdbcTemplate
     *
     * @param dataSource 数据源
     * @return JdbcTemplate对象
     */
    @Bean(name = "internalJdbcTemplate")
    public JdbcTemplate internalJdbcTemplate(@Qualifier("internalDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "internalSessionFactory")
    @Primary
    public SqlSessionFactory internalSessionFactory(@Qualifier("internalDataSource") DataSource datasource)
            throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis/internal/*.xml"));
        return bean.getObject();
    }

    @Bean("internalSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate internalSqlSessiontemplate(
            @Qualifier("internalSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    @Bean(name = "internalTransactionManager")
    @Primary
    public DataSourceTransactionManager internalTransactionManager(@Qualifier("internalDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Configuration
    public class InternalAdaptor {
        @Bean(name = "internalOutsideServiceAdaptor")
        public OutsideServiceAdaptor internalOutsideServiceAdaptor(TraceReportExternalMysqlService traceReportExternalMysqlService) {
            return new InternalOutsideServiceAdaptor(traceReportExternalMysqlService);
        }
    }

}

