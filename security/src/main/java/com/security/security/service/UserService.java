package com.security.security.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pojo.User;

/**
 * @author haya
 */
@FeignClient(value = "user-service")
public interface UserService {

    @GetMapping(value = "user/{name}/{password}")
    User get(@PathVariable String name, @PathVariable String password);

    @GetMapping(value = "user/{username}/{email}")
    Boolean findPwd(@PathVariable String username, @PathVariable String email);

    @PostMapping(value = "/user")
    Object save(@RequestBody User user);

    @PostMapping(value = "/user/{id}/{np}/{op}")
    boolean setPassword(@PathVariable Integer id,
                              @PathVariable String np,
                              @PathVariable String op);
}
