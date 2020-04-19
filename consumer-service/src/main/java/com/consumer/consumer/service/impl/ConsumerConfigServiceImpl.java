package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.ConsumerConfigMapper;
import com.consumer.consumer.mapper.ConsumerMapper;
import com.consumer.consumer.service.ConsumerConfigService;
import com.consumer.consumer.service.ConsumerService;
import org.springframework.stereotype.Service;
import pojo.Consumer;
import pojo.ConsumerConfig;

/**
 * @author haya
 */
@Service
public class ConsumerConfigServiceImpl extends ServiceImpl<ConsumerConfigMapper, ConsumerConfig> implements ConsumerConfigService {
}
