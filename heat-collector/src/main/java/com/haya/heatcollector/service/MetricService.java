package com.haya.heatcollector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haya.heatcollector.entity.Metric;

public interface MetricService extends IService<Metric> {
    Metric selectElseInsert(Metric metric);

    Metric test(Metric metric);
}
