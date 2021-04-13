package com.consumer.consumer;

import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.mapper.druid.DruidMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ConsumerServiceApplicationTests {
    @Autowired
    private DruidMapper druidMapper;

    @Test
    void contextLoads() {
        List<HeatDataDTO> test = druidMapper.test();
        System.out.println( test.size() );
    }

    @Test
    void getComponentByIdList() {
        ArrayList<Integer> idList = new ArrayList<>();
        List<HeatDataDTO> test = druidMapper.selectByComponentIdList(idList, new Integer[]{1});
        System.out.println( test.size() );
    }
}
