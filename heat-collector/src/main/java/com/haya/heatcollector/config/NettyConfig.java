package com.haya.heatcollector.config;

import com.haya.heatcollector.handle.MetricHandle;
import com.haya.heatcollector.netty.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

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
        server.start( new InetSocketAddress( "127.0.0.1", 9616 ) );
    }

}
