package com.consumer.consumer.controller;

import com.consumer.consumer.service.ConsumerConfigService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
