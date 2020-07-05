package com.consumer.consumer.controller;

import annotation.LogInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.service.GrpsService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pojo.Grps;

import java.util.Date;
import java.util.List;

/**
 * @author haya
 */
@RestController
public class GrpsController {
    @Autowired
    private GrpsService grpsService;

    @GetMapping(value = "/grps/all")
    public Object getAll() {
        return grpsService.list();
    }

    @LogInfo(value = "分页获取站点信息")
    @GetMapping(value = "/grps/page/{pageNo}/{pageSize}")
    public Object page(@PathVariable Integer pageNo, @PathVariable Integer pageSize) {
        return MessageFactory.message( true, grpsService.page( new Page<>( pageNo, pageSize ) ) );
    }

    @LogInfo(value = "根据no分页获取站点信息")
    @GetMapping(value = "/grps/{no}/page/{pageNum}/{pageSize}")
    public Object pageByNo(@PathVariable Integer no,
                           @PathVariable Integer pageNum,
                           @PathVariable Integer pageSize) {
        QueryWrapper<Grps> wrapper = new QueryWrapper<>();
        wrapper.like( "no", no );
        return MessageFactory.message(true, grpsService.pageMaps( new Page<>( pageNum, pageSize ), wrapper ));
    }

    @PostMapping(value = "/grps")
    public Object save(@RequestBody Grps grps) {
        grps.setCreateDate( new Date() );
        grps.setOnline( false );
        return MessageFactory.message( grpsService.saveOrUpdate( grps ) );
    }


    @DeleteMapping(value = "/grps")
    public Object deleteById(@RequestBody List<Integer> idList) {
        return MessageFactory.message( grpsService.removeByIds( idList ) );
    }

    @GetMapping(value = "/grps/list")
    public Object deleteById() {
        return MessageFactory.message(true, grpsService.list() );
    }
}
