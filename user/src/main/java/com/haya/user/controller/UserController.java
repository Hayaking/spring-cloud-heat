package com.haya.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haya.user.service.UserService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.User;

import javax.ws.rs.POST;

/**
 * @author haya
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{name}/{password}")
    public User find4login(@PathVariable String name, @PathVariable String password) {
        System.out.println( name );
        return userService.getUser( name, password );
    }

    @GetMapping(value = "/user/all")
    public Object getAll() {
        return userService.list();
    }

    @GetMapping(value = "/page/{pageNo}/{pageSize}")
    public Object getByPage(@PathVariable Integer pageNo, @PathVariable Integer pageSize) {
        return MessageFactory.message( true, userService.page( new Page<>( pageNo, pageSize ) ) );
    }

    /**
     * 添加或更新
     * @param user
     * @return
     */
    @PostMapping(value = "/user")
    public Object save(@RequestBody User user) {
        return userService.saveOrUpdate( user );
    }

    /**
     * 逻辑删除
     * @param id 用户id
     * @return
     */
    @DeleteMapping(value = "/user/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return userService.removeById( id );
    }

    /**
     * 更改用户角色信息
     * @param id 用户id
     * @return
     */
    @PostMapping(value = "/user/{id}/{roleId}")
    public Object updateRole(@PathVariable Integer id) {
        return userService.removeById( id );
    }
}
