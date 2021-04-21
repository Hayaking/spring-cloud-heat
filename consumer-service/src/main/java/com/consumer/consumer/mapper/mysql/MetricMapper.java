package com.consumer.consumer.mapper.mysql;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pojo.Metric;

@Mapper
public interface MetricMapper extends BaseMapper<Metric> {
}
