package com.security.security.service;

import com.security.security.service.hystrix.UserServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pojo.User;

/**
 * @author haya
 */
@FeignClient(value = "user-service",fallback = UserServiceHystrix.class)
public interface UserService {
    @GetMapping(value = "user/{name}/{password}")
    User get(@PathVariable String name, @PathVariable String password);
}
