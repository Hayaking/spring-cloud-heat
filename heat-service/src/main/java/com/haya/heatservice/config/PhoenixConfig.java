package com.haya.heatservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author haya
 */
@Configuration
public class PhoenixConfig {

    @Bean(name = "phoenixDaraSource")
    @ConfigurationProperties(prefix = "spring.datasource.phoenix")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    @Bean
    @ConfigurationProperties(prefix="mybatis-plus")
    public MybatisConfiguration mybatisConfig() {
        return new MybatisConfiguration();
    }

    @Bean(name = "phoenixSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("phoenixDaraSource") DataSource dataSource) throws Exception {
        return new MybatisSqlSessionFactoryBean() {{
            MybatisConfiguration config = new MybatisConfiguration();
            config.setMapUnderscoreToCamelCase( false );
            setDataSource( dataSource );
            setConfiguration( config );
        }}.getObject();
    }

    @Bean(name = "phoenixTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("phoenixDaraSource") DataSource dataSource) {
        return new DataSourceTransactionManager( dataSource );
    }

    @Bean(name = "phoenixSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("phoenixSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate( sqlSessionFactory );
    }

}
