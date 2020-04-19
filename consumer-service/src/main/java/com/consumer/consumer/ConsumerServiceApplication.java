package com.consumer.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author haya
 */
@MapperScan(basePackages="com.haya.user.mapper")
@SpringBootApplication
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run( ConsumerServiceApplication.class, args );
    }

}
