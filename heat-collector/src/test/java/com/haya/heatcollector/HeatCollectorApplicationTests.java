package com.haya.heatcollector;

import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.service.MetricService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class HeatCollectorApplicationTests {
    @Autowired
    private MetricService metricService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        Metric metric = new Metric();
        metric.setType(1);
        metric.setName("test");
        for (int i = 0; i < 1; i++) {
            System.out.println(metricService.test(metric));
        }
    }

    @Test
    void get() {
        Metric metric = (Metric) redisTemplate.opsForValue().get("metric::1:test");
        System.out.println(metric);
    }

    @Test
    void merge() {
        contextLoads();
        get();
    }


}
