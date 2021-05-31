package com.consumer.consumer;

import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.mapper.phoenix.PhoenixMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ConsumerServiceApplicationTests {
    @Autowired
    private PhoenixMapper phoenixMapper;

    @Test
    void contextLoads() {
        List<HeatDataDTO> test = phoenixMapper.test();
        System.out.println( test.size() );
    }

    @Test
    void getComponentByIdList() {
//        ArrayList<Integer> idList = new ArrayList<>();
//        List<HeatDataDTO> test = druidMapper.selectByComponentIdList(idList, new Integer[]{1});
//        System.out.println( test.size() );
    }
}
