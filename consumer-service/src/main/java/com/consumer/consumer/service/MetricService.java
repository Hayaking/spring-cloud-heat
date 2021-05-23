package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.MetricVO;
import pojo.Component;
import pojo.Metric;

import java.util.List;

public interface MetricService extends IService<Metric> {
    Metric selectElseInsert(Metric metric);

    Page<MetricVO> getMetricPage(ComponentFilter filter);

    List<Component> getComponentListById(Integer id);

    ChartResponse getDetailChart(DruidParam druidParam);
}
