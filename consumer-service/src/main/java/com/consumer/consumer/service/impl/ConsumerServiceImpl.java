package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.ConsumerMapper;
import com.consumer.consumer.service.ConsumerService;
import org.springframework.stereotype.Service;
import pojo.Consumer;

/**
 * @author haya
 */
@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {
}
