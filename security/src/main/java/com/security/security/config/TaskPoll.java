package com.security.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
@Configuration
public class TaskPoll {
    @Bean
    public ThreadPoolExecutor taskExecutePoll() {
        return new ThreadPoolExecutor(
                4,
                10,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>( 10 ),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
