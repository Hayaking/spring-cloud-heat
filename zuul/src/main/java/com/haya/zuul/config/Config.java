package com.haya.zuul.config;

import com.haya.zuul.handler.Handler;
import com.haya.zuul.handler.impl.SecurityHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haya
 */
@Configuration
public class Config {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        return new RestTemplate( requestFactory );
    }

    @Bean
    public Map<String, Handler> handlerMap() {
        return new HashMap<String, Handler>() {{
            put( "security", new SecurityHandler() );
        }};
    }
}
