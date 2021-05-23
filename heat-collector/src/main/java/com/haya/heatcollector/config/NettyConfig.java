package com.haya.heatcollector.config;

import com.haya.heatcollector.netty.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @author haya
 */
@Configuration
public class NettyConfig {
    @Autowired
    private Server server;
    @Value( "${collector.ip}" )
    private String ip;
    @Value( "${collector.port}" )
    private Integer port;

    @PostConstruct
    public void server() {
        new Thread(() -> {
            server.start( new InetSocketAddress( ip, port ) );
        }).start();

    }

}
