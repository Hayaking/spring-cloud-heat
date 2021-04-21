package com.haya.heatcollector.handle;


import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.Alarm;
import com.haya.heatcollector.entity.AlarmConfig;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.entity.Metric;
import com.haya.heatcollector.service.AlarmConfigService;
import com.haya.heatcollector.service.AlarmService;
import com.haya.heatcollector.service.ComponentService;
import com.haya.heatcollector.service.MetricService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
@Data
@Service
public class MetricHandle {
    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ThreadPoolExecutor taskExecutePoll;

    public void handle(HeatData heatData) {
        System.out.println(heatData);
        Component component = componentService.selectElseInsert(heatData);
        String key = "component:up:" + component.getId();
        Metric metric = new Metric() {{
            setType(heatData.getType());
            setName(heatData.getMetricName());
        }};
        // 指标注册
        metric = metricService.selectElseInsert(metric);
        if ("component_up".equals(metric.getName())) {
            Set<String> upSet = redisTemplate.opsForZSet().rangeByScore(key, 0, 3);
            Double up = 0D;
            if (!CollectionUtils.isEmpty(upSet)) {
                up = upSet.stream()
                        .mapToDouble(Double::parseDouble)
                        .max()
                        .orElse(0);
            }
            component.setUp(up.intValue());
            componentService.updateById(component);
            heatData.setMetricValue(up);
            System.out.println(heatData);
        } else {
            List<AlarmConfig> configList = alarmConfigService.select(component, metric);
            if (!CollectionUtils.isEmpty(configList)) {
                for (AlarmConfig config : configList) {
                    Double bottom = config.getBottom();
                    Double top = config.getTop();
                    Double metricValue = heatData.getMetricValue();
                    if (metricValue >= top || metricValue <= bottom) {
                        Alarm alarm = new Alarm();
                        alarm.setLevel(config.getLevel());
                        alarm.setMetricId(metric.getId());
                        alarm.setComponentId(component.getId());
                        alarm.setConfigId(config.getId());
                        alarm.setMetricValue(metricValue);
                        alarm.setCtime(new Date());
                        alarmService.save(alarm);
                        redisTemplate.opsForZSet()
                                .add(key, config.getLevel().toString(), config.getLevel());
                        redisTemplate.expire(key, 30, TimeUnit.SECONDS);
                    }
                }
            }
        }

        heatData.setComponentName(component.getName());
        heatData.setStreet(component.getStreet());
        heatData.setArea(component.getArea());
        heatData.setId(component.getId());
        heatData.setArea(component.getArea());
        heatData.setStreet(component.getStreet());
        template.send("data1", JSON.toJSONString(heatData));
    }
}
