package com.haya.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haya.user.common.msg.MessageFactory;
import com.haya.user.common.pojo.User;
import com.haya.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/user")
    public Boolean save(@RequestBody User user) {
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


    @PostMapping(value = "/user/{id}/{np}/{op}")
    public Boolean setPassword(@PathVariable Integer id,
                              @PathVariable String np,
                              @PathVariable String op) {
        return userService.setNewPassword(id, op, np);
    }


    @GetMapping(value = "/{username}/{email}")
    public Boolean findPwd(@PathVariable String username,
                               @PathVariable String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq( "username", username )
                .eq( "email", email );
        User user = userService.getOne( wrapper );
        if (user != null) {
            return userService.setPassword(user.getId(), email);
        }
        return false;
    }
}
