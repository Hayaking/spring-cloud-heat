package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.vo.AlarmConfigVO;
import com.consumer.consumer.mapper.mysql.AlarmConfigMapper;
import com.consumer.consumer.service.AlarmConfigService;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.service.MetricService;
import com.consumer.consumer.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.AlarmConfig;
import pojo.Component;
import pojo.Metric;
import pojo.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmConfigServiceImpl extends ServiceImpl<AlarmConfigMapper, AlarmConfig> implements AlarmConfigService {
    @Autowired
    private AlarmConfigMapper alarmConfigMapper;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    @Override
    public Page<AlarmConfigVO> getAlarmConfigPage(Page<AlarmConfig> page) {
        Page<AlarmConfig> list = alarmConfigMapper.selectPage(page, new QueryWrapper<>());
        Page<AlarmConfigVO> result = new Page<>();
        result.setSize(list.getSize());
        result.setTotal(list.getTotal());
        result.setCurrent(list.getCurrent());
        List<AlarmConfigVO> collect = list.getRecords().stream().map(item -> {
            AlarmConfigVO vo = new AlarmConfigVO();
            BeanUtils.copyProperties(item, vo);
            Component component = componentService.getById(item.getComponentId());
            Metric metric = metricService.getById(item.getId());
            User user = userService.getById(item.getUserId());
            vo.setMetricName(metric.getName());
            vo.setComponentType(component.getType());
            vo.setComponentName(component.getName());
            vo.setUserName(user.getUsername());
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(collect);
        return result;
    }

    @Override
    public boolean removeWithCacheById(Integer id) {
        try {
            AlarmConfig config = alarmConfigMapper.selectById(id);
            alarmConfigMapper.deleteById(id);
            String key = "alarm:config::" + config.getComponentId() + ":" + config.getMetricId();
            redisTemplate.delete(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
