package com.consumer.consumer.controller;

import annotation.LogInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.HeatMapData;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.service.ComponentService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Component;

import java.util.List;

import static java.util.Arrays.asList;

@RequestMapping("/component")
@RestController
public class ComponentController {
    @Autowired
    private DruidMapper druidMapper;
    @Autowired
    private ComponentService componentService;

    @GetMapping("/test")
    public Object test() {
        return druidMapper.test();
    }

    @LogInfo(type = "post", value = "获取监控实例分页列表")
    @PostMapping("/page")
    public Message getComponentPage(@RequestBody ComponentFilter filter) {
        Page<ComponentVO> componentPage = componentService.getComponentPage(filter);
        return MessageFactory.message(true, componentPage);
    }

    @LogInfo(type = "get", value = "获取首页监控实列列表")
    @GetMapping("/list")
    public Message getComponentList() {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.notIn("type", asList(9, 10, 11));
        List<Component> list = componentService.list(wrapper);
        return MessageFactory.message(true, list);
    }

    @LogInfo(type = "get", value = "根据名字搜索监控实例")
    @GetMapping("/list/name/{name}")
    public Message getComponentList(@PathVariable String name) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.like("name", name);
        return MessageFactory.message(true, componentService.list(wrapper));
    }

    @PostMapping("/update")
    public Message update(@RequestBody ComponentVO componentVO) {
        UpdateWrapper<Component> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", componentVO.getId());
        wrapper.set("name", componentVO.getName());
        wrapper.set("note", componentVO.getNote());
        wrapper.set("area", componentVO.getArea());
        wrapper.set("street", componentVO.getStreet());
        wrapper.set("next_id", componentVO.getNextId());
        boolean isUpdate = componentService.update(wrapper);
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
