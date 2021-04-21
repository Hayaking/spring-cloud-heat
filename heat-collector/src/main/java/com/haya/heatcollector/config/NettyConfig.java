package com.haya.heatcollector.config;

import com.haya.heatcollector.netty.Server;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostConstruct
    public void server() {
        new Thread(() -> {
            server.start( new InetSocketAddress( "127.0.0.1", 9616 ) );
        }).start();

    }

}
