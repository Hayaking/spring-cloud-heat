package com.security.security.controller;

import annotation.LogInfo;
import com.security.security.service.JWTService;
import com.security.security.service.UserService;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import pojo.User;

/**
 * @author haya
 */
@RequestMapping(value = "base")
@RestController
public class SecurityController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "/login")
    public Object login(@RequestBody User user) {
        User u = userService.getUser(user.getUsername(), user.getPassword());
        if (u != null) {
            return MessageFactory.message(true, jwtService.createToken(u));
        }
        return MessageFactory.message(false);
    }

    @LogInfo(value = "登出", type = "POST")
    @PostMapping(value = "/logout")
    public Object logout() {
        return MessageFactory.message(true);
    }

    @GetMapping(value = "/user")
    public Object login() {
        return MessageFactory.message(true, jwtService.getInfo());
    }

    @GetMapping(value = "/cookie/{cookie}")
    public Object getByCookie(@PathVariable String cookie) {
        return null;
    }

    @LogInfo(value = "登陆成功之后设置新密码，使用旧密码进行对比")
    @PostMapping(value = "/password")
    public Object setPassword(@RequestParam(value = "oldPassword", defaultValue = "uid") String oldPassword,
                              @RequestParam(value = "passWord", defaultValue = "passWord") String passWord) {
        // 获取登陆者信息
        return MessageFactory.message(userService.setNewPassword(null, oldPassword, passWord));
    }

    @LogInfo(value = "找回密码")
    @PostMapping(value = "/pwd")
    public Object findPwd(@RequestParam(value = "username") String username,
                          @RequestParam(value = "email") String email) {
        return MessageFactory.message(true, userService.getUserByEmail(username, email));
    }

    @GetMapping(value = "/verify")
    public Object verify(@RequestParam(value = "username") String username,
                         @RequestParam(value = "email") String email) {
        return MessageFactory.message(true, userService.getUserByEmail(username, email));
    }

    @GetMapping(value = "/userInfo", consumes = "application/json")
    public User userInfo() {
        return jwtService.getInfo();
    }
}
