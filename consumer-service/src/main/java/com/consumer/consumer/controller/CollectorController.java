package com.consumer.consumer.controller;

import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.vo.CollectorVO;
import com.consumer.consumer.service.CollectorService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collector")
public class CollectorController {
    @Autowired
    private CollectorService collectorService;

    @PostMapping("/page")
    public Message getComponentPage(@RequestBody ComponentFilter filter) {
        List<CollectorVO> componentPage = collectorService.getPage(filter);
        return MessageFactory.message(true, componentPage);
    }

}
