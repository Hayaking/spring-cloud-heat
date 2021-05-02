package com.haya.heatcollector.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.Component;


public interface ComponentService extends IService<Component> {

    Component selectElseInsert(HeatData heatData);
}
