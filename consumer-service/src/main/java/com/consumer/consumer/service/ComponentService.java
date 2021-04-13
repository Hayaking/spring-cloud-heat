package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import pojo.Component;

public interface ComponentService extends IService<Component> {
    void drawChart();

    Page<ComponentVO> getComponentPage(ComponentFilter filter);

    ComponentBaseInfoVO getBaseInfo(Integer id);

    ChartResponse druid(DruidParam druidParam);
}
