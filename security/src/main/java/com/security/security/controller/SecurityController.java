package com.security.security.controller;

import annotation.LogInfo;
import com.security.security.service.UserService;
import com.security.security.utils.JWTUtil;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import pojo.User;

/**
 * @author haya
 */
@RestController
public class SecurityController {
    @Autowired
    private UserService userService;
    //    @Autowired
//    private Map<Serializable, Object> pool;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "/login")
    public Object login(@RequestBody User user) {
        User u = userService.get( user.getUsername(), user.getPassword() );
        if (u != null) {
            return MessageFactory.message( true, JWTUtil.createToken( user ) );
        }
        return MessageFactory.message( false );
    }

    @LogInfo(value = "登出", type = "POST")
    @PostMapping(value = "/logout")
    public Object logout() {
//        SecurityUtils.getSubject().logout();
        return MessageFactory.message( true );
    }

    //    @LogInfo(value = "获取个人信息")
    @GetMapping(value = "/user")
    public Object login() {
        return MessageFactory.message( true, JWTUtil.getInfo() );
//        Subject subject = SecurityUtils.getSubject();
//        Object principal = subject.getPrincipal();
//        return MessageFactory.message( true, principal );
    }

    @LogInfo(value = "登陆后修改个人信息")
    @PutMapping(value = "/user")
    public Object update(@RequestBody User user) {
//        user.setId( ((User) SecurityUtils.getSubject().getPrincipal()).getId() );
        return MessageFactory.message( true, userService.save( user ) );
    }

    //    @LogInfo(value = "登录",type = "POST")
    @GetMapping(value = "/cookie/{cookie}")
    public Object getByCookie(@PathVariable String cookie) {
        return null;
//        return MessageFactory.message( true, pool.get( cookie ) );
    }

    @LogInfo(value = "登陆成功之后设置新密码，使用旧密码进行对比")
    @PostMapping(value = "/password")
    public Object setPassword(@RequestParam(value = "oldPassword", defaultValue = "uid") String oldPassword,
                              @RequestParam(value = "passWord", defaultValue = "passWord") String passWord) {
        // 获取登陆者信息
//        User user = ((User) SecurityUtils.getSubject().getPrincipal());
        return MessageFactory.message( userService.setPassword( null, oldPassword, passWord ) );
    }

    @LogInfo(value = "找回密码")
    @PostMapping(value = "/pwd")
    public Object findPwd(@RequestParam(value = "username") String username,
                          @RequestParam(value = "email") String email) {
        return MessageFactory.message( userService.findPwd( username, email ) );
    }
}
