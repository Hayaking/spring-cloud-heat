package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.service.ConsumerConfigService;
import com.consumer.consumer.service.ConsumerService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import pojo.Consumer;

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

    @PutMapping(value = "consumer")
    @PostMapping(value = "consumer")
    public Object save(@RequestBody Consumer consumer) {
        return consumerService.saveOrUpdate( consumer );
    }

    @DeleteMapping(value = "consumer/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return consumerService.removeById( id );
    }

    @KafkaListener(id = "consumer", topics = "topic.q2")
    public void warn(String message) {
        consumerService.snedWarnEmail( message );
        System.out.println( message );
    }
}
