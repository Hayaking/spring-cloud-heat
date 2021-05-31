package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.HeatMapData;
import pojo.Component;
import pojo.Metric;

import java.util.List;

public interface ComponentService extends IService<Component> {
    void drawChart();

    ChartResponse draw(DruidParam druidParam, Metric metric);

    Page<ComponentVO> getComponentPage(ComponentFilter filter);

    ComponentBaseInfoVO getBaseInfo(Integer id);

    ChartResponse druid(DruidParam druidParam);

    List<HeatMapData> getHeatMap();

    Double sumHourFlow();

    List getTop5(String metric);

    List<ComponentVO> getHomeComponentList(String name);

    Boolean updateComponent(ComponentVO componentVO);
}
