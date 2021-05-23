package com.security.security.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.security.security.bean.ComponentFilter;
import com.security.security.bean.vo.UserVo;
import com.security.security.service.JWTService;
import com.security.security.service.UserService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.User;

import java.util.List;

/**
 * @author haya
 */
@RequestMapping(value = "user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @GetMapping(value = "/{name}/{password}")
    public User find4login(@PathVariable String name, @PathVariable String password) {
        System.out.println(name);
        return userService.getUser(name, password);
    }

    @GetMapping(value = "/all")
    public Object getAll() {
        return userService.list();
    }

    @PostMapping("/page")
    public Object getByPage(@RequestBody ComponentFilter filter) {
        IPage<UserVo> page = userService.getUserPage( filter );
        return MessageFactory.message( true, page );
    }

    @PostMapping(value = "/save")
    public Boolean save(@RequestBody User user) {
        return userService.saveOrUpdate(user);
    }

    /**
     * 逻辑删除
     *
     * @param id 用户id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    /**
     * 更改用户角色信息
     *
     * @param id 用户id
     * @return
     */
    @PostMapping(value = "/{id}/{roleId}")
    public Object updateRole(@PathVariable Integer id) {
        return userService.removeById(id);
    }


    @PostMapping(value = "/{id}/{np}/{op}")
    public Boolean setPassword(@PathVariable Integer id,
                               @PathVariable String np,
                               @PathVariable String op) {
        return userService.setNewPassword(id, op, np);
    }


    @GetMapping(value = "/{username}/{email}")
    public Boolean findPwd(@PathVariable String username,
                           @PathVariable String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("email", email);
        User user = userService.getOne(wrapper);
        if (user != null) {
            return userService.setPassword(user.getId(), email);
        }
        return false;
    }

    @PostMapping(value = "/enable/{id}")
    public Boolean findPwd(@PathVariable Integer id) {
        return userService.setUserEnable(id);
    }

    @GetMapping(value = "/list/alarm")
    public Message getAlarmUserList() {
        List<User> list = userService.list();
        return MessageFactory.message(true, list);
    }

    @GetMapping(value = "/self")
    public Message self() {
        User info = jwtService.getInfo();
        int id = info.getId();
        User user = userService.getById(id);
        return MessageFactory.message(true, user);
    }

    @DeleteMapping(value = "/batch")
    public Message deleteBatch(@RequestBody List<Integer> idList) {
        boolean flag = userService.removeByIds(idList);
        return MessageFactory.message(flag);
    }

}
