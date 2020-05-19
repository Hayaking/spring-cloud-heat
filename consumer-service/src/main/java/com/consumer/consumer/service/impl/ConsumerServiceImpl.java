package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.ConsumerMapper;
import com.consumer.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import pojo.Consumer;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haya
 */
@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {
    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Resource
    private ConsumerMapper consumerMapper;
    private final Map<String, String> stringMap = new HashMap<String, String>() {{
        put( "flow", "流量" );
        put( "temp", "温度" );
        put( "pres", "压力" );
        put( "low", "过小" );
        put( "high", "过大" );
    }};

    @Override
    public boolean snedWarnEmail(String message) {
        String[] token = message.split( "," );
        String name, state;
        name = stringMap.get( token[0] );
        state = stringMap.get( token[2] );
        int consumerId = Integer.parseInt( token[1] );
//        consumerMapper.getEmailbyId()
        javaMailSender.send( new SimpleMailMessage() {{
            setSubject( "==异常==" + name + state );
            setText( "==异常==" + name + state);
            setTo( "" );
            setFrom( "1028779917@qq.com" );
        }} );
        return false;
    }
}
