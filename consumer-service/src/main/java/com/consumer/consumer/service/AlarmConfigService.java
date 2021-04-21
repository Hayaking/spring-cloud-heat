package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.vo.AlarmConfigVO;
import pojo.AlarmConfig;

public interface AlarmConfigService extends IService<AlarmConfig> {
    Page<AlarmConfigVO> getAlarmConfigPage(Page<AlarmConfig> page);

    boolean removeWithCacheById(Integer id);
}
