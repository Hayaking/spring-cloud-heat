package com.haya.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pojo.User;

/**
 * @author haya
 */
public interface UserService extends IService<User> {
    Boolean isExist(String username, String password);

    User getUser(String username, String password);

    User getUserByEmail(String username, String email);

    /**
     * 设置新密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    boolean setNewPassword(int id, String oldPassword, String newPassword);

    boolean setPassword(int id, String email);
}
