package com.haya.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author gyl
 * @date 2020/5/27 11:05
 */
public class SendEmailUtils {

    @Autowired
    private static JavaMailSenderImpl javaMailSender;
    @Value("${spring.mail.username}")
    private static String from;
    /**
     * 发送邮箱验证码
     *
     * @param to   接受的邮箱
     * @param text 验证码信息
     */
    public static void sendEmail(String to, String text) {
        javaMailSender.send(new SimpleMailMessage(){{
            setSubject("===找回密码===");
            setText(text);
            setTo(to);
            setFrom(from);
        }});
    }
}
