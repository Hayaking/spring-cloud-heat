package com.haya.heatcollector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haya.heatcollector.entity.AlarmConfig;
import com.haya.heatcollector.entity.Component;
import com.haya.heatcollector.entity.Metric;

import java.util.List;


public interface AlarmConfigService extends IService<AlarmConfig> {
    List<AlarmConfig> select(Component component, Metric metric);
}
