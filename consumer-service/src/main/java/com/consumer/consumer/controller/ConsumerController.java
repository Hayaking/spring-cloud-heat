package com.consumer.consumer.controller;

import com.consumer.consumer.service.ConsumerConfigService;
import com.consumer.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "consumer/all")
    public Object getAll() {
        return consumerService.list();
    }

    @PostMapping(value = "consumer")
    public Object save(@RequestBody Consumer consumer) {
        return consumerService.saveOrUpdate( consumer );
    }

    @DeleteMapping(value = "consumer/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return consumerService.removeById( id );
    }
}
