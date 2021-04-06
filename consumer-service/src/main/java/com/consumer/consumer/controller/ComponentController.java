package com.consumer.consumer.controller;

import com.consumer.consumer.mapper.druid.MinuteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComponentController {
    @Autowired
    private MinuteMapper minuteMapper;
    @GetMapping("/test")
    public Object test() {
        return minuteMapper.test();
    }
}
