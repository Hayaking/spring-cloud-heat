package com.security.security.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haya
 */
@RestController
public class SecurityController {

    @PostMapping(value = "/login")
    public Object login(@RequestBody UsernamePasswordToken token) {
        System.out.println( token );
        Subject subject = SecurityUtils.getSubject();
        subject.login( token );
        return SecurityUtils.getSubject().getSession().getId();
    }
}
