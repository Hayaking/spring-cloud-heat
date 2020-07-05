package com.security.security.controller;

import annotation.LogInfo;
import com.security.security.service.UserService;
import msg.MessageFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.User;

import java.io.Serializable;
import java.util.Map;

/**
 * @author haya
 */
@RestController
public class SecurityController {
    @Autowired
    private UserService userService;
    @Autowired
    private Map<Serializable, Object> pool;

    @LogInfo(value = "登录",type = "POST")
    @PostMapping(value = "/login")
    public Object login(@RequestBody UsernamePasswordToken token) {
        Subject subject = SecurityUtils.getSubject();
        subject.login( token );
        pool.put( SecurityUtils.getSubject().getSession().getId(),
                SecurityUtils.getSubject().getPrincipal() );
        return MessageFactory.message( true, SecurityUtils.getSubject().getSession().getId() );
    }

    @LogInfo(value = "登出",type = "POST")
    @PostMapping(value = "/logout")
    public Object logout() {
        SecurityUtils.getSubject().logout();
        return MessageFactory.message( true );
    }

    @LogInfo(value = "获取个人信息")
    @GetMapping(value = "/user")
    public Object login() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return MessageFactory.message( true, principal );
    }

    @LogInfo(value = "登陆后修改个人信息")
    @PutMapping(value = "/user")
    public Object update(@RequestBody User user) {
        user.setId(((User) SecurityUtils.getSubject().getPrincipal()).getId());
        return MessageFactory.message( true, userService.save(user) );
    }

    @LogInfo(value = "登录",type = "POST")
    @GetMapping(value = "/cookie/{cookie}")
    public Object getByCookie(@PathVariable String cookie) {
        return MessageFactory.message( true, pool.get( cookie ) );
    }

    @LogInfo(value = "登陆成功之后设置新密码，使用旧密码进行对比")
    @PostMapping(value = "/password")
    public Object setPassword(@RequestParam(value = "oldPassword", defaultValue = "uid") String oldPassword,
                                    @RequestParam(value = "passWord", defaultValue = "passWord") String passWord) {
        // 获取登陆者信息
        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        return MessageFactory.message( userService.setPassword( user.getId(), oldPassword, passWord ) );
    }

    @LogInfo(value = "找回密码")
    @PostMapping(value = "/pwd")
    public Object findPwd(@RequestParam(value = "username") String username,
                          @RequestParam(value = "email") String email) {
        return MessageFactory.message( userService.findPwd( username, email ) );
    }
}
