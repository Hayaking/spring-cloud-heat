package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.vo.AlarmConfigVO;
import com.consumer.consumer.mapper.mysql.AlarmConfigMapper;
import com.consumer.consumer.service.AlarmConfigService;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.service.MetricService;
import com.consumer.consumer.service.UserService;
import com.consumer.consumer.util.WrapperUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pojo.AlarmConfig;
import pojo.Component;
import pojo.Metric;
import pojo.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlarmConfigServiceImpl extends ServiceImpl<AlarmConfigMapper, AlarmConfig> implements AlarmConfigService {
    @Autowired
    private AlarmConfigMapper alarmConfigMapper;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private MetricService metricService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    @Override
    public Page<AlarmConfigVO> getAlarmConfigPage(ComponentFilter filter) {
        Page<AlarmConfig> page = new Page<>( filter.getPageNum(), filter.getPageSize() );
        QueryWrapper<AlarmConfig> wrapper = new QueryWrapper<>();
        WrapperUtil.buildCondition( filter, wrapper );
        wrapper.orderByDesc( "mtime", "ctime" );
        page = alarmConfigMapper.selectPage(page, wrapper);
        Page<AlarmConfigVO> result = new Page<>();
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        List<AlarmConfigVO> collect = page.getRecords().stream().map(item -> {
            AlarmConfigVO vo = new AlarmConfigVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(collect);
        return result;
    }

    @Override
    public boolean removeCacheById(Integer id) {
        try {
            AlarmConfig config = alarmConfigMapper.selectById(id);
            String key = "alarm:config::" + config.getComponentId() + ":" + config.getMetricId();
            redisTemplate.delete(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveConfig(AlarmConfig config) {
        Integer userId = config.getUserId();
        Integer metricId = config.getMetricId();
        Integer componentId = config.getComponentId();
        if (userId == null || metricId == null || componentId == null) {
            return false;
        }
        User user = userService.getById( userId );
        Metric metric = metricService.getById( metricId );
        Component component = componentService.getById( componentId );
        config.setUserName( user.getUsername() );
        config.setMetricName( metric.getName() );
        config.setComponentType( component.getType() );
        config.setComponentName( component.getName() );
        boolean flag = saveOrUpdate( config );
        if (ObjectUtils.isEmpty( config.getId() )) {
            removeCacheById( config.getId() );
        }
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Integer> idList) {
        boolean flag = removeByIds( idList );
        idList.forEach( this::removeCacheById );
        return flag;
    }
}
