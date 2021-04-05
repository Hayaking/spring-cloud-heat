package com.haya.heatcollector;

import com.haya.heatcollector.netty.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

/**
 * @author haya
 */
@SpringBootApplication
public class HeatCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run( HeatCollectorApplication.class, args );
    }


}
