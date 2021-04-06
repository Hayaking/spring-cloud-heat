package com.consumer.consumer;

import com.consumer.consumer.bean.HeatDataDTO;
import com.consumer.consumer.mapper.druid.MinuteMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ConsumerServiceApplicationTests {
    @Autowired
    private MinuteMapper minuteMapper;

    @Test
    void contextLoads() {
        List<HeatDataDTO> test = minuteMapper.test();
        System.out.println( test.size() );
    }

}
