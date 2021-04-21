package com.consumer.consumer.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.mysql.UserMapper;
import com.consumer.consumer.service.UserService;
import org.springframework.stereotype.Service;
import pojo.User;

/**
 * @author haya
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
