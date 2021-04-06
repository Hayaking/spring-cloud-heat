package com.consumer.consumer.config;

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
public class ApacheDruidConfig {

    @Bean(name = "druidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
    @Bean
    @ConfigurationProperties(prefix="mybatis-plus")
    public MybatisConfiguration mybatisConfig() {
        return new MybatisConfiguration();
    }

    @Bean(name = "druidSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource) throws Exception {
        return new MybatisSqlSessionFactoryBean() {{
            MybatisConfiguration config = new MybatisConfiguration();
            config.setMapUnderscoreToCamelCase( false );
            setDataSource( dataSource );
            setConfiguration( config );
        }}.getObject();
    }

    @Bean(name = "druidTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("druidDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager( dataSource );
    }

    @Bean(name = "druidSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("druidSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate( sqlSessionFactory );
    }

}
