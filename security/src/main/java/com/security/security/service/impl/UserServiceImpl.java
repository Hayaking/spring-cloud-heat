package com.security.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.security.mapper.UserMapper;
import com.security.security.service.JWTService;
import com.security.security.service.UserService;
import com.security.security.utils.SendEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pojo.User;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author haya
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ThreadPoolExecutor taskExecutePoll;

    @Override
    public Boolean isExist(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq( "username", username ).eq( "password", password );
        return null == userMapper.selectOne( wrapper );
    }

    @Override
    public User getUser(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq( "username", username )
                .eq( "password", password );
        return userMapper.selectOne( wrapper );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean getUserByEmail(String username, String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("email", email);
        boolean flag = false;
        try {
            User user = userMapper.selectOne(wrapper);
            flag = user != null;
        } catch (Exception e) {
            return false;
        }
        if (flag) {
            taskExecutePoll.execute(()->{
                StringBuffer newPassword = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    newPassword.append(Integer.toHexString(new Random().nextInt(16)));
                }
                int res = userMapper.updateByUserNameAndEmail(username, email, newPassword.toString());
                if (res == 1) {
                    javaMailSender.send(new SimpleMailMessage(){{
                        setFrom("1028779917@qq.com");
                        setTo(email);
                        setSubject("[热力监控系统]新密码");
                        setText("新密码：" + newPassword.toString());
                    }});
                }
            });
        }
        return flag;
    }


    @Override
    public boolean setNewPassword(Integer id, String oldPassword, String newPassword) {

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

    @Override
    public IPage<User> getUserPage(IPage<User> page, String key) {
        User info = jwtService.getInfo();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (info.getRoleId() == 0) {
            wrapper.notIn("role_id", 0);
        } else if (info.getRoleId() == 1) {
            wrapper.notIn("role_id", 0, 1);
        }
        wrapper.notIn("id", info.getId());
        if (!StringUtils.isEmpty(key)) {
            wrapper.like("username", key)
                    .or()
                    .like("phone", key)
                    .or()
                    .like("email", key);
        }
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    public Boolean setUserEnable(Integer id) {
        return 1== userMapper.setUserEnable(id);
    }

    @Override
    public List<User> getAlarmUserList() {
//        User info = jwtService.getInfo();
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        return userMapper.selectList( wrapper);
    }
}
