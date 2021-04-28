package com.security.security.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.security.service.LogService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Log;

import java.util.List;

/**
 * @author haya
 */
@RequestMapping(value = "log")
@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @DeleteMapping(value = "/delete/all")
    public Object clean() {
        return null;
    }

    @DeleteMapping(value = "/batch")
    public Message deleteByIds(@RequestBody List<Integer> idList) {
        return MessageFactory.message( logService.removeByIds( idList ) );
    }

    @GetMapping(value = "/page/{pageNum}/{pageSize}")
    public Object page(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_date");
        Page<Log> page = logService.page(new Page<>(pageNum, pageSize), wrapper);
        page.setSize(pageSize);
        return MessageFactory.message(true, page);
    }

    @GetMapping(value = "/page/{pageNum}/{pageSize}/{startDate}/{endDate}")
    public Object pageDateRange(@PathVariable Integer pageNum,
                       @PathVariable Integer pageSize,
                       @PathVariable String startDate,
                       @PathVariable String endDate) {
        IPage<Log> page = logService.getPageByDateRange(new Page<>(pageNum, pageSize), startDate, endDate);
        page.setSize(pageSize);
        return MessageFactory.message( true, page);
    }

}
