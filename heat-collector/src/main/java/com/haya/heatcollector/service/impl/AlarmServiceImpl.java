package com.haya.heatcollector.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.heatcollector.entity.Alarm;
import com.haya.heatcollector.mapper.AlarmMapper;
import com.haya.heatcollector.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;


}
