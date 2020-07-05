package com.consumer.consumer.controller;

import annotation.LogInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.consumer.consumer.service.ConsumerConfigService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.ConsumerConfig;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.management.ManagementFactory;

/**
 * @author haya
 */
@RestController
public class ConsumerConfigController {
    @Autowired
    private ConsumerConfigService configService;

    @GetMapping(value = "config/list")
    public Object list() {
        return MessageFactory.message( true, configService.getListWithConsumer() );
    }

    @LogInfo(value = "根据consumer的id获取config")
    @GetMapping(value = "{id}/config")
    public Object getByConsumerId(@PathVariable Integer id) {
        return MessageFactory.message( true, configService.getByConsumerId(id));
    }

//    @LogInfo(value = "插入config")
//
//    @PutMapping(value = "config")
//    public Object add(@RequestBody ConsumerConfig config) {
//        return MessageFactory.message( configService.save( config ) );
//    }

    @LogInfo(value = "更新config",getParam = false)
    @PostMapping(value = "config")
    @PutMapping(value = "config")
    public Object update(@RequestBody ConsumerConfig config) {
        QueryWrapper<ConsumerConfig> wrapper = new QueryWrapper<>();
        wrapper.eq( "consumer_id", config.getConsumerId() );
        ConsumerConfig one = configService.getOne( wrapper );
        if (one != null) {
            config.setId( one.getId() );
        } else {
            config.setId( null );
        }
        return MessageFactory.message( configService.saveOrUpdate( config ) );
    }

    @LogInfo(value = "根据id查询config")
    @GetMapping(value = "config/{id}")
    public Object getById(@PathVariable Integer id) {
        return MessageFactory.message( true, configService.getById( id ));
    }
}
