package com.security.security.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.security.security.service.LogService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Log;

import java.util.List;

/**
 * @author haya
 */
@RestController
public class LogController {
    @Autowired
    private LogService logService;

    @DeleteMapping(value = "/log/delete/all")
    public Object clean() {
        return null;
    }

    @DeleteMapping(value = "/log/delete")
    public Object deleteByIds(@RequestBody List<Integer> ids) {
        return MessageFactory.message( logService.removeByIds( ids ) );
    }

    @GetMapping(value = "/log/page/{pageNum}/{pageSize}")
    public Object page(@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
        Page<Log> page = logService.page(new Page<>(pageNum, pageSize));
        return MessageFactory.message(true, page);
    }

    @GetMapping(value = "/log/page/{pageSize}/{pageNum}/{startDate}/{endDate}")
    public Object pageDateRange(@PathVariable Integer pageSize,
                       @PathVariable Integer pageNum,
                       @PathVariable String startDate,
                       @PathVariable String endDate) {
        return MessageFactory.message( true,logService.getPageByDateRange(
                new Page<>( pageNum, pageSize ),
                startDate,
                endDate ) );
    }

}
