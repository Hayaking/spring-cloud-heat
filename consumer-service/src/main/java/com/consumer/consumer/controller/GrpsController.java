package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.service.GrpsService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pojo.Grps;

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

    @GetMapping(value = "/grps/page/{pageNo}/{pageSize}")
    public Object page(@PathVariable Integer pageNo, @PathVariable Integer pageSize) {
        return MessageFactory.message( true, grpsService.page( new Page<>( pageNo, pageSize ) ) );
    }

    @PostMapping(value = "/grps")
    public Object save(@RequestBody Grps grps) {
        return grpsService.saveOrUpdate( grps );
    }


    @DeleteMapping(value = "/grps/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return grpsService.removeById( id );
    }

    @GetMapping(value = "/grps/list")
    public String deleteById(Model model) {
        return "board";
    }
}
