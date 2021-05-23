package com.consumer.consumer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.vo.AlarmConfigVO;
import pojo.AlarmConfig;

import java.util.List;

public interface AlarmConfigService extends IService<AlarmConfig> {
    Page<AlarmConfigVO> getAlarmConfigPage(ComponentFilter filter);

    boolean removeCacheById(Integer id);

    boolean saveConfig(AlarmConfig config);

    boolean deleteByIds(List<Integer> idList);
}
