package com.consumer.consumer.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import pojo.Consumer;
import pojo.ConsumerConfig;

/**
 * @author haya
 */
@Mapper
public interface ConsumerConfigService extends IService<ConsumerConfig> {
}
