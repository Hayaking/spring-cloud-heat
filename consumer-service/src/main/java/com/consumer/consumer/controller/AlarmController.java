package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.ComponentFilter;
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

    @PostMapping(value = "/list")
    public Message getList(@RequestBody ComponentFilter filter) {
        Page<AlarmVO> page = alarmService.getAlarmPage( filter );
        return MessageFactory.message( true, page );
    }

    @PostMapping(value = "/list/config")
    public Message getConfigList(@RequestBody ComponentFilter filter) {
        Page<AlarmConfigVO> page = alarmConfigService.getAlarmConfigPage(filter);
        return MessageFactory.message(true, page);
    }

    @PostMapping(value = "/save")
    public Message save(@RequestBody AlarmConfig config) {
        boolean isSave = alarmConfigService.saveConfig(config);
        return MessageFactory.message(isSave);
    }

    @DeleteMapping(value = "/config/batch")
    public Message delete(@RequestBody List<Integer> idList) {
        boolean isDelete = alarmConfigService.deleteByIds( idList );
        return MessageFactory.message(isDelete);
    }

    @DeleteMapping(value = "/batch")
    public Message batch(@RequestBody List<Integer> idList) {
        boolean isDelete = alarmService.removeByIds(idList);
        return MessageFactory.message(isDelete);
    }
}
