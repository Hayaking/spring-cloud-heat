package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import pojo.ConsumerConfig;

import java.util.List;

/**
 * @author haya
 */
@Mapper
public interface ConsumerConfigService extends IService<ConsumerConfig> {
    List<ConsumerConfig> getListWithConsumer();
}
