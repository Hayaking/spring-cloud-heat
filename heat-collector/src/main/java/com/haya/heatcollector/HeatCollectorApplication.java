package com.haya.heatcollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author haya
 */
@EnableCaching
@SpringBootApplication
public class HeatCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run( HeatCollectorApplication.class, args );
    }


}
