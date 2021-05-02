package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.service.PipelineService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Pipeline;

import java.util.List;

@RestController
@RequestMapping("/pipeline")
public class PipelineController {
    @Autowired
    private PipelineService pipelineService;

    @PostMapping("/save")
    public Message save(@RequestBody PipelineVo pipeline) {
        pipelineService.savePipeline( pipeline );
        return MessageFactory.message(pipelineService.saveOrUpdate(pipeline));
    }

    @GetMapping("/list")
    public Message list() {
        List<Pipeline> list = pipelineService.list();
        return MessageFactory.message(true, list);
    }

    @GetMapping(value = {
            "/page/{pageNum}/{pageSize}/{key}",
            "/page/{pageNum}/{pageSize}"
    })
    public Message getAllByPage(@PathVariable Integer pageNum,
                                @PathVariable Integer pageSize,
                                @PathVariable(required = false) String key) {
        Page<PipelineVo> page = pipelineService.getPage( pageNum, pageSize, key );
        return MessageFactory.message(true, page);
    }

    @DeleteMapping(value = "/{id}")
    public Message getAllByPage(@PathVariable Integer id) {
        boolean res = pipelineService.removeById(id);
        return MessageFactory.message(res);
    }

    @DeleteMapping(value = "/batch")
    public Message getAllByPage(@RequestBody List<Integer> idList) {
        boolean res = pipelineService.removeByIds(idList);
        return MessageFactory.message(res);
    }
}
