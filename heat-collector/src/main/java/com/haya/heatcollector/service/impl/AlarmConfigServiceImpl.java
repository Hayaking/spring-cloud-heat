package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.entity.AlarmConfig;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.mapper.AlarmConfigMapper;
import com.haya.heatcollector.service.AlarmConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "alarm:config")
@Service
public class AlarmConfigServiceImpl extends ServiceImpl<AlarmConfigMapper, AlarmConfig> implements AlarmConfigService {

    @Autowired
    private AlarmConfigMapper alarmConfigMapper;

    @Cacheable(key = "#component.id+':'+#metric.id")
    @Override
    public List<AlarmConfig> select(Component component, Metric metric) {
        QueryWrapper<AlarmConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("component_id", component.getId());
        wrapper.eq("metric_id", metric.getId());
        return alarmConfigMapper.selectList(wrapper);
    }
}
