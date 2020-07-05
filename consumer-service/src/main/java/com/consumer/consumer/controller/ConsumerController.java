package com.consumer.consumer.controller;

import annotation.LogInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.service.ConsumerConfigService;
import com.consumer.consumer.service.ConsumerService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import pojo.Consumer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author haya
 */
@RestController
public class ConsumerController {

    @Autowired
    public ConsumerService consumerService;
    @Autowired
    public ConsumerConfigService configService;

    @GetMapping(value = "/list")
    public Object list() {
        return MessageFactory.message( true, consumerService.list());
    }

    @GetMapping(value = "/page/{pageNum}/{pageSize}")
    public Object page(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        IPage<Consumer> res = consumerService.page( new Page<>( pageNum, pageSize ) );
        return MessageFactory.message( true, res);
    }


    @LogInfo(value = "根据name模糊分页查询consumer")
    @GetMapping(value = "/page/{name}/{pageNum}/{pageSize}")
    public Object pageByName(@PathVariable String name,@PathVariable Integer pageNum, @PathVariable Integer pageSize ) {
        QueryWrapper<Consumer> wrapper = new QueryWrapper<>();
        wrapper.like( "name", name );
        IPage<Consumer> res = consumerService.page( new Page<>( pageNum, pageSize ) ,wrapper);
        return MessageFactory.message( true, res);
    }

    @LogInfo(value = "添加一个consumer")
    @PutMapping(value = "")
    @PostMapping(value = "")
    public Object save(@RequestBody Consumer consumer) {
        consumer.setCreateDate( new Date() );
        return MessageFactory.message( consumerService.saveOrUpdate( consumer ) );
    }

    @LogInfo(value = "批量删除consumer", type = "DELETE")
    @DeleteMapping(value = "")
    public Object deleteBatchByIds(@RequestBody List<Integer> idList) {
        return MessageFactory.message( consumerService.removeByIds( idList ) );
    }

//    @KafkaListener(id = "consumer2", topics = "topic.q2")
//    public void warn(String message) {
//        consumerService.snedWarnEmail( message );
//        System.out.println( message );
//    }
}
