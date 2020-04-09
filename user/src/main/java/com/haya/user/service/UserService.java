package com.haya.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pojo.User;

/**
 * @author haya
 */
public interface UserService extends IService<User> {
    Boolean isExist(String username, String password);

    User getUser(String username, String password);
}
