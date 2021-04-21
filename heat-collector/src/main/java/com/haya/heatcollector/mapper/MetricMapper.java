package com.haya.heatcollector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haya.heatcollector.entity.Metric;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MetricMapper extends BaseMapper<Metric> {
}
