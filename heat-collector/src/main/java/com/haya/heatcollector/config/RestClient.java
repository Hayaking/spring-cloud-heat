package com.haya.heatcollector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author haya
 */
@Configuration
public class RestClient {
    @Bean
    public static RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout( 30000 );
        requestFactory.setReadTimeout( 30000 );
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory( requestFactory );
        return restTemplate;
    }

}
