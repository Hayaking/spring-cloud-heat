package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.mapper.druid.DruidMapper;
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
    private DruidMapper druidMapper;
    @Autowired
    private ComponentService componentService;

    @GetMapping("/test")
    public Object test() {
        return druidMapper.test();
    }

    @PostMapping("/page")
    public Message getComponentPage(@RequestBody ComponentFilter filter) {
        Page<ComponentVO> componentPage = componentService.getComponentPage(filter);
        return MessageFactory.message(true, componentPage);
    }

    @GetMapping("/list")
    public Message getComponentList() {
        List<Component> list = componentService.list();
        return MessageFactory.message(true, list);
    }

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
}
