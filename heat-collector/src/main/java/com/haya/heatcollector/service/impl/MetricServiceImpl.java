package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.mapper.MetricMapper;
import com.haya.heatcollector.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author haya
 */
@Service
public class MetricServiceImpl extends ServiceImpl<MetricMapper, Metric> implements MetricService {
    @Autowired
    private MetricMapper metricMapper;
    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = "metric", key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    public Metric selectElseInsert(Metric metric) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        wrapper.eq("name", metric.getName())
                .eq("type", metric.getType());
        Metric res = metricMapper.selectOne(wrapper);
        if (res == null) {
            Metric component = new Metric();
            component.setName(metric.getName());
            component.setType(metric.getType());
            metricMapper.insert(component);
            res = metricMapper.selectOne(wrapper);
        }
        return res;
    }

    @CachePut(cacheNames = "metric", key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    @Override
    public Metric test(Metric metric) {
        System.out.println("~~~~~~~");
        return metric;
    }
}
