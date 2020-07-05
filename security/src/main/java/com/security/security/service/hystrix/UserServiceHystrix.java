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

    @Override
    public Boolean findPwd(String username, String email) {
        return false;
    }

    @Override
    public Object save(User user) {
        return null;
    }

    @Override
    public boolean setPassword(Integer id, String np, String op) {
        return false;
    }
}
