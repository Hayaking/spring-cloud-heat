package com.consumer.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author haya
 */
@EnableKafka
@EnableFeignClients
@EnableEurekaClient
@MapperScans( value = {
        @MapperScan(basePackages = "com.consumer.consumer.mapper.mysql",sqlSessionFactoryRef = "mysqlSqlSessionFactory"),
        @MapperScan(basePackages = "com.consumer.consumer.mapper.phoenix",sqlSessionFactoryRef = "phoenixSqlSessionFactory")
})
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run( ConsumerServiceApplication.class, args );
    }

}
