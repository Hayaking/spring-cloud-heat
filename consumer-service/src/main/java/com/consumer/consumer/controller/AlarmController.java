package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.vo.AlarmConfigVO;
import com.consumer.consumer.bean.vo.AlarmVO;
import com.consumer.consumer.service.AlarmConfigService;
import com.consumer.consumer.service.AlarmService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.AlarmConfig;

import java.util.List;

@RestController
@RequestMapping("/alarm")
public class AlarmController {
    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/list/{pageNum}/{pageSize}/{id}")
    public Message getListById(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               @PathVariable Integer id) {
        Page<AlarmVO> page = alarmService.getAlarmPage(new Page<>(pageNum, pageSize), id);
        return MessageFactory.message(true, page);
    }

    @GetMapping(value = "/list/{pageNum}/{pageSize}")
    public Message getList(@PathVariable Integer pageNum,
                           @PathVariable Integer pageSize) {
        Page<AlarmVO> page = alarmService.getAlarmPage(new Page<>(pageNum, pageSize), null);
        return MessageFactory.message(true, page);
    }

    @GetMapping(value = "/list/config/{pageNum}/{pageSize}")
    public Message getConfigList(@PathVariable Integer pageNum,
                                 @PathVariable Integer pageSize) {
        Page<AlarmConfigVO> page = alarmConfigService.getAlarmConfigPage(new Page<>(pageNum, pageSize));
        return MessageFactory.message(true, page);
    }

    @PostMapping(value = "/save")
    public Message save(@RequestBody AlarmConfig config) {
        boolean isSave = alarmConfigService.saveOrUpdate(config);
        return MessageFactory.message(isSave);
    }

    @DeleteMapping(value = "/config/batch")
    public Message delete(@RequestBody List<Integer> idList) {
        boolean isDelete = alarmConfigService.removeByIds(idList);
        return MessageFactory.message(isDelete);
    }

    @DeleteMapping(value = "/batch")
    public Message batch(@RequestBody List<Integer> idList) {
        boolean isDelete = alarmService.removeByIds(idList);
        return MessageFactory.message(isDelete);
    }
}
