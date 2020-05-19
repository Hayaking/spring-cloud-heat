package com.security.security.controller;

import com.security.security.service.UserService;
import msg.MessageFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pojo.User;

/**
 * @author haya
 */
@RestController
public class SecurityController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Object login(@RequestBody UsernamePasswordToken token) {
        System.out.println( token );
        Subject subject = SecurityUtils.getSubject();
        subject.login( token );
        return MessageFactory.message( true, SecurityUtils.getSubject().getSession().getId() );
    }

    @PostMapping(value = "/logout")
    public Object logout() {
        SecurityUtils.getSubject().logout();
        return MessageFactory.message( true );
    }

    @GetMapping(value = "/user")
    public Object login() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();

        return MessageFactory.message( true, principal );
    }

}
