package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.mysql.MetricMapper;
import com.consumer.consumer.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pojo.Metric;

/**
 * @author haya
 */
@Service
@CacheConfig(cacheNames = "metric")
public class MetricServiceImpl extends ServiceImpl<MetricMapper, Metric> implements MetricService {
    @Autowired
    private MetricMapper metricMapper;

    @Override
    @Cacheable(key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    public Metric selectElseInsert(Metric metric) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        wrapper.eq( "name", metric.getName() )
                .eq( "type", metric.getType() );
        Metric res = metricMapper.selectOne( wrapper );
        if (res == null) {
            Metric component = new Metric();
            component.setName( metric.getName() );
            component.setType( metric.getType() );
            metricMapper.insert( component );
            res = metricMapper.selectOne( wrapper );
        }
        return res;
    }
}
