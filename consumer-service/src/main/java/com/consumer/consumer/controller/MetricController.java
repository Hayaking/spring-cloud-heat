package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.consumer.consumer.service.MetricService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Metric;

@RestController
@RequestMapping("/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;

    @GetMapping(value = "/list/{type}")
    public Message list(@PathVariable Integer type) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        return MessageFactory.message(true, metricService.list(wrapper));
    }

}
