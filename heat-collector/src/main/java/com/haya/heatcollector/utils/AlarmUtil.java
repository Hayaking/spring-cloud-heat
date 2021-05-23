package com.haya.heatcollector.utils;

import com.haya.heatcollector.entity.*;
import com.haya.heatcollector.mapper.UserMapper;
import com.haya.heatcollector.service.AlarmService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class AlarmUtil {

    private static final AlarmService alarmService = SpringBeanUtil.getBean( AlarmService.class );
    private static final JavaMailSender javaMailSender = SpringBeanUtil.getBean( JavaMailSender.class );
    private static final UserMapper userMapper = SpringBeanUtil.getBean( UserMapper.class );


    /**
     * 发送邮件
     *
     * @param component
     * @param metric
     * @param metricValue
     * @param config
     */
    public static void sendMail(Component component, Metric metric, Double metricValue, AlarmConfig config) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper( mimeMessage );
        Integer uid = config.getUserId();
        User targetUser = userMapper.selectById( uid );
        try {
            message.setFrom( "1028779917@qq.com" );
            message.setTo( targetUser.getEmail() );
            StringBuffer subject = new StringBuffer( "[警告]" );
            subject.append( component.getName() );
            subject.append( "-" );
            subject.append( metric.getName() );
            subject.append( "-" );
            subject.append( metricValue );
            message.setSubject( subject.toString() );
            StringBuffer text = new StringBuffer();
            text.append( subject );
            text.append( "\n" );
            text.append( component );
            text.append( "\n" );
            message.setText( text.toString() );
            javaMailSender.send( mimeMessage );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送警报
     *
     * @param config
     * @param component
     * @param metricValue
     * @param metric
     */
    public static void saveAlarm(AlarmConfig config, Component component, Double metricValue, Metric metric) {
        // 保存告警
        Alarm alarm = new Alarm();
        alarm.setLevel( config.getLevel() );
        alarm.setMetricId( metric.getId() );
        alarm.setComponentId( component.getId() );
        alarm.setConfigId( config.getId() );
        alarm.setMetricName( StringUtils.isEmpty( metric.getAliasName() ) ? metric.getName() : metric.getAliasName() );
        alarm.setMetricValue( metricValue );
        alarm.setCtime( new Date() );
        alarmService.save( alarm );
    }
}
