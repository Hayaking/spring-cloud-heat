package com.consumer.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.vo.AlarmVO;
import pojo.Alarm;

public interface AlarmService extends IService<Alarm> {

    Page<AlarmVO> getAlarmPage(Page<Alarm> page, Integer id);
}
