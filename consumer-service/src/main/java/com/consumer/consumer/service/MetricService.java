package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pojo.Metric;

public interface MetricService extends IService<Metric> {
    Metric selectElseInsert(Metric metric);
}
