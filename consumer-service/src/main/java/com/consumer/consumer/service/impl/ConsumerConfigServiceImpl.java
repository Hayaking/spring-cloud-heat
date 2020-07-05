package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.ConsumerConfigMapper;
import com.consumer.consumer.service.ConsumerConfigService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;
import pojo.ConsumerConfig;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author haya
 */

@Service
public class ConsumerConfigServiceImpl extends ServiceImpl<ConsumerConfigMapper, ConsumerConfig> implements ConsumerConfigService {
    @Resource
    private ConsumerConfigMapper consumerConfigMapper;

    @Override
    public List<ConsumerConfig> getListWithConsumer() {
        return consumerConfigMapper.getListWithConsumer();
    }

    @Override
    public ConsumerConfig getByConsumerId(Integer id) {
        QueryWrapper<ConsumerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq( "consumer_id", id );
        return consumerConfigMapper.selectOne( wrapper );

    }
}
