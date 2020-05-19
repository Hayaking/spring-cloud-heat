package com.security.security.config;

//import com.alibaba.druid.pool.DruidDataSource;
//import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haya
 */
//@Configuration
public class MySQLConfig {
//    @Bean(name = "mysqlDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.mysql")
//    public DataSource dataSource() {
//        return new DruidDataSource();
//    }
//
//    @Bean(name = "mysqlSqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
//        return new MybatisSqlSessionFactoryBean() {{
//            setDataSource( dataSource );
//        }}.getObject();
//    }
//
//    @Bean(name = "mysqlTransactionManager")
//    public DataSourceTransactionManager transactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager( dataSource );
//    }
//
//    @Bean(name = "mysqlSqlSessionTemplate")
//    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate( sqlSessionFactory );
//    }

}
