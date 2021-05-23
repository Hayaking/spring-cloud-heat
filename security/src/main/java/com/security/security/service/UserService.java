package com.security.security.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.security.security.bean.ComponentFilter;
import com.security.security.bean.vo.UserVo;
import pojo.User;

import java.util.List;

/**
 * @author haya
 */
public interface UserService extends IService<User> {
    Boolean isExist(String username, String password);

    User getUser(String username, String password);

    boolean getUserByEmail(String username, String email);

    /**
     * 设置新密码
     *
     * @param id          用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    boolean setNewPassword(Integer id, String oldPassword, String newPassword);

    boolean setPassword(int id, String email);

    IPage<UserVo> getUserPage(ComponentFilter filter);

    Boolean setUserEnable(Integer id);

    List<User> getAlarmUserList();

}
