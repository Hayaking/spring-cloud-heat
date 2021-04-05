package com.security.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.redis.core.RedisTemplate;


@ServletComponentScan
@SpringBootTest
class SecurityApplicationTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    void testRedis() {
        String key = "test:user:1024";
        String value = "haya";
        redisTemplate.opsForValue().set( key, value );
        String content = (String) redisTemplate.opsForValue().get( key );
        System.out.println(value);
        System.out.println(content);
    }

}
