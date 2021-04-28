package com.haya.heatcollector.handle;


import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.entity.*;
import com.haya.heatcollector.mapper.UserMapper;
import com.haya.heatcollector.service.AlarmConfigService;
import com.haya.heatcollector.service.AlarmService;
import com.haya.heatcollector.service.ComponentService;
import com.haya.heatcollector.service.MetricService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
@Data
@Service
public class MetricHandle {
    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private AlarmConfigService alarmConfigService;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ThreadPoolExecutor taskExecutePoll;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserMapper userMapper;

    public void handle(HeatData heatData) {

        Component component = componentService.selectElseInsert(heatData);
        String key = "component:up:set:" + component.getId();
        Metric metric = registerMetric(heatData);

        if ("component_up".equals(metric.getName())) {
            taskExecutePoll.execute(() -> makeComponentUp(key, component, heatData));
        } else {
            List<AlarmConfig> configList = alarmConfigService.select(component, metric);
            if (!CollectionUtils.isEmpty(configList)) {
                for (AlarmConfig config : configList) {
                    Double bottom = config.getBottom();
                    Double top = config.getTop();
                    Double metricValue = heatData.getMetricValue();
                    if (metricValue >= top || metricValue <= bottom) {
                        // 保存告警
                        taskExecutePoll.execute(() -> saveAlarm(config, component, metricValue, metric));
                        // 添加异常状态到set
                        taskExecutePoll.execute(() -> saveUptoZSet(key, config));
                        // 发送邮件
                        taskExecutePoll.execute(() -> sendMail(component, metric, metricValue, config));
                    }
                }
            }
        }

        heatData.setComponentName(component.getName());
        heatData.setStreet(component.getStreet());
        heatData.setArea(component.getArea());
        heatData.setId(component.getId());
        heatData.setArea(component.getArea());
        heatData.setStreet(component.getStreet());
        template.send("data1", JSON.toJSONString(heatData));
    }

    private void saveUptoZSet(String key, AlarmConfig config) {
        redisTemplate.opsForZSet().add(key, config.getLevel().toString(), config.getLevel());
        redisTemplate.expire(key, 30, TimeUnit.SECONDS);
    }


    public Metric registerMetric(HeatData heatData) {
        Metric metric = new Metric() {{
            setType(heatData.getType());
            setName(heatData.getMetricName());
        }};
        // 指标注册
        return metricService.selectElseInsert(metric);
    }

    public void makeComponentUp(String key, Component component, HeatData heatData) {
        Set<String> upSet = redisTemplate.opsForZSet().rangeByScore(key, 0, 3);
        Double up = 0D;
        if (!CollectionUtils.isEmpty(upSet)) {
            up = upSet.stream()
                    .mapToDouble(Double::parseDouble)
                    .max()
                    .orElse(0);
        }
        heatData.setMetricValue(up);
        redisTemplate.opsForValue().set("component:up:" + component.getId(), String.valueOf(up.intValue()));
        redisTemplate.expire("component:up:" + component.getId(), 70, TimeUnit.SECONDS);
    }

    public void sendMail(Component component, Metric metric, Double metricValue, AlarmConfig config) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        Integer uid = config.getUserId();
        User targetUser = userMapper.selectById(uid);
        try {
            message.setFrom("1028779917@qq.com");
            message.setTo(targetUser.getEmail());
            StringBuffer subject = new StringBuffer("[警告]");
            subject.append(component.getName());
            subject.append("-");
            subject.append(metric.getName());
            subject.append("-");
            subject.append(metricValue);
            message.setSubject(subject.toString());
            StringBuffer text = new StringBuffer();
            text.append(subject.toString());
            text.append("\n");
            text.append(component.toString());
            text.append("\n");
            message.setText(text.toString());
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void saveAlarm(AlarmConfig config, Component component, Double metricValue, Metric metric) {
        // 保存告警
        Alarm alarm = new Alarm();
        alarm.setLevel(config.getLevel());
        alarm.setMetricId(metric.getId());
        alarm.setComponentId(component.getId());
        alarm.setConfigId(config.getId());
        alarm.setMetricValue(metricValue);
        alarm.setCtime(new Date());
        alarmService.save(alarm);
    }
}
