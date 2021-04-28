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

import java.util.List;

public interface ComponentService extends IService<Component> {
    void drawChart();

    Page<ComponentVO> getComponentPage(ComponentFilter filter);

    ComponentBaseInfoVO getBaseInfo(Integer id);

    void setPipeBaseInfo(Integer id, ComponentBaseInfoVO baseInfo);

    void setPumpBaseInfo(Integer id, ComponentBaseInfoVO baseInfo);

    void setSensorBaseInfo(Integer id, ComponentBaseInfoVO baseInfo);

    void setStationBaseInfo(Integer id, ComponentBaseInfoVO baseInfo);

    ChartResponse druid(DruidParam druidParam);

    List<HeatMapData> getHeatMap();

    Double sumHourFlow();

    List getTop5(String metric);
}
