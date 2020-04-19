package com.haya.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.user.mapper.UserMapper;
import com.haya.user.service.UserService;
import org.springframework.stereotype.Service;
import pojo.User;

import javax.annotation.Resource;

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
}
