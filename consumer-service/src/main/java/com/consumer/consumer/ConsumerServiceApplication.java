package com.consumer.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author haya
 */
@EnableKafka
@EnableEurekaClient
@MapperScan(basePackages="com.consumer.consumer.mapper")
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run( ConsumerServiceApplication.class, args );
    }

}
