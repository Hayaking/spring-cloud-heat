package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.vo.AlarmVO;
import com.consumer.consumer.mapper.mysql.AlarmConfigMapper;
import com.consumer.consumer.mapper.mysql.AlarmMapper;
import com.consumer.consumer.service.AlarmService;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.service.MetricService;
import com.consumer.consumer.service.UserService;
import com.consumer.consumer.util.DoubleFormatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.Alarm;
import pojo.Component;
import pojo.Metric;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;
    @Autowired
    private AlarmConfigMapper alarmConfigMapper;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    @Override
    public Page<AlarmVO> getAlarmPage(Page<Alarm> page, Integer id) {
        QueryWrapper<Alarm> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("ctime");
        if (id != null) {
            wrapper.eq("component_id", id);
        }
        Page<Alarm> list = alarmMapper.selectPage(page, wrapper);
        Page<AlarmVO> result = new Page<>();
        result.setSize(list.getSize());
        result.setTotal(list.getTotal());
        result.setCurrent(list.getCurrent());
        List<AlarmVO> collect = list.getRecords().stream().map(item -> {
            AlarmVO vo = new AlarmVO();
            BeanUtils.copyProperties(item, vo);
            vo.setMetricValue(DoubleFormatUtil.halfUp(item.getMetricValue()));
//            AlarmConfig config = alarmConfigMapper.selectById(item.getConfigId());
            Component component = componentService.getById(item.getComponentId());
            Metric metric = metricService.getById(item.getMetricId());
//            vo.setLevel(config.getLevel());
            vo.setMetricName(metric.getName());
            vo.setComponentId(component.getId());
            vo.setComponentType(component.getType());
            vo.setComponentName(component.getName());
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(collect);
        return result;

    }
}
