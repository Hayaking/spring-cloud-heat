package com.haya.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.user.mapper.UserMapper;
import com.haya.user.service.UserService;
import com.haya.user.utils.SendEmailUtils;
import org.springframework.stereotype.Service;
import pojo.User;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author haya
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean isExist(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq( "username", username ).eq( "password", password );
        return null == userMapper.selectOne( wrapper );
    }

    @Override
    public User getUser(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq( "username", username ).eq( "password", password );
        return userMapper.selectOne( wrapper );
    }

    @Override
    public User getUserByEmail(String username, String email) {
        return null;
    }

    @Override
    public boolean setNewPassword(int id, String oldPassword, String newPassword) {

        User user = userMapper.selectById( id );
        if (user == null) {
            return false;
        } else {
            if (!user.getPassword().equals(oldPassword)) {
                // 旧密码错误
                return false;
            }
            user.setPassword( newPassword );
            return user.updateById();
        }
    }

    @Override
    public boolean setPassword(int id, String email) {
        User user = userMapper.selectById( id );
        // 生成新密码
        String code = UUID.randomUUID().toString().split("-")[0];
        // 新密码加密
        user.setPassword( code );
        // 发送邮件
        SendEmailUtils.sendEmail(email, "新密码：" + code);
        return true;
    }
}
