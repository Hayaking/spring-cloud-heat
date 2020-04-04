package com.security.security.service.hystrix;

import com.security.security.service.UserService;
import org.springframework.stereotype.Component;
import pojo.User;

/**
 * @author haya
 */
@Component
public class UserServiceHystrix implements UserService {
    @Override
    public User get(String name, String password) {
        return null;
    }
}
