package com.consumer.consumer.service;

import com.consumer.consumer.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import pojo.User;

/**
 * @author haya
 */
@FeignClient(value = "security",configuration = FeignConfiguration.class)
public interface SecurityClient {

    @GetMapping("/base/verify")
    boolean verify();

    @GetMapping(value = "/base/userInfo",consumes = "application/json")
    User getUserInfo();
}
