package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.HeatMapData;
import com.consumer.consumer.mapper.phoenix.PhoenixMapper;
import com.consumer.consumer.service.ComponentService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Component;

import java.util.List;

@RequestMapping("/component")
@RestController
public class ComponentController {
    @Autowired
    private PhoenixMapper phoenixMapper;
    @Autowired
    private ComponentService componentService;

//    @LogInfo(type = "post", value = "获取监控实例分页列表")
    @PostMapping("/page")
    public Message getComponentPage(@RequestBody ComponentFilter filter) {
        Page<ComponentVO> componentPage = componentService.getComponentPage(filter);
        return MessageFactory.message(true, componentPage);
    }

//    @LogInfo(type = "get", value = "获取首页监控实列列表")
    @GetMapping(value = {
            "/list",
            "/list/name/{name}"
    })
    public Message getComponentList(@PathVariable(required = false) String name) {
        List<ComponentVO> list = componentService.getHomeComponentList(name);
        return MessageFactory.message(true, list);
    }

    @GetMapping(value = "/list/sensor")
    public Message getComponentList() {
        List<Component> list = componentService.list();
        return MessageFactory.message(true, list);
    }

    @PostMapping("/update")
    public Message update(@RequestBody ComponentVO componentVO) {
        Boolean isUpdate = componentService.updateComponent( componentVO );

        return MessageFactory.message(isUpdate);
    }

    @GetMapping("/baseInfo/{id}")
    public Message baseInfo(@PathVariable Integer id) {
        ComponentBaseInfoVO baseInfo = componentService.getBaseInfo(id);
        return MessageFactory.message(true, baseInfo);
    }

    @PostMapping("/druid")
    public ChartResponse druid(@RequestBody DruidParam druidParam) {
        return componentService.druid(druidParam);
    }

    @GetMapping("/heat")
    public Message getHeatData() {
        List<HeatMapData> list = componentService.getHeatMap();
        return MessageFactory.message(false, list);
    }

    @GetMapping("/hour/sum/flow")
    public Message sumFlow() {
        Double result = componentService.sumHourFlow();
        return MessageFactory.message(false, result);
    }


    @GetMapping("/top5/{metric}")
    public Message top5(@PathVariable String metric) {
        List result =  componentService.getTop5(metric);
        return MessageFactory.message(false, result);
    }
}
