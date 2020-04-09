package com.haya.user.controller;

import com.haya.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pojo.User;

/**
 * @author haya
 */
@RestController
public class SecurityController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/security/get/{name}/{password}")
    public User xxxx(@PathVariable String name, @PathVariable String password) {
        System.out.println( name );
        return userService.getUser( name, password );
    }
}
