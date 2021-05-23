package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.MetricVO;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.mapper.mysql.MetricMapper;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.service.MetricService;
import com.consumer.consumer.util.WrapperUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pojo.Component;
import pojo.Metric;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haya
 */
@Service
@CacheConfig(cacheNames = "metric")
public class MetricServiceImpl extends ServiceImpl<MetricMapper, Metric> implements MetricService {
    @Autowired
    private MetricMapper metricMapper;
    @Autowired
    private DruidMapper druidMapper;
    @Autowired
    private ComponentService componentService;

    @Override
    @Cacheable(key = "#metric.type+':'+#metric.name", condition = "#result!=null")
    public Metric selectElseInsert(Metric metric) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        wrapper.eq( "name", metric.getName() )
                .eq( "type", metric.getType() );
        Metric res = metricMapper.selectOne( wrapper );
        if (res == null) {
            Metric component = new Metric();
            component.setName( metric.getName() );
            component.setType( metric.getType() );
            metricMapper.insert( component );
            res = metricMapper.selectOne( wrapper );
        }
        return res;
    }

    @Override
    public Page<MetricVO> getMetricPage(ComponentFilter filter) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        WrapperUtil.buildCondition( filter, wrapper );
        wrapper.orderByDesc( "ctime" );
        Page<Metric> page = page( new Page<>( filter.getPageNum(), filter.getPageSize() ), wrapper );
        Page<MetricVO> result = new Page<>();
        result.setSize( page.getSize() );
        result.setTotal( page.getTotal() );
        result.setCurrent( page.getCurrent() );
        List<MetricVO> list = page.getRecords().stream().map( item -> {
            MetricVO vo = new MetricVO();
            BeanUtils.copyProperties( item, vo );
            return vo;
        } ).collect( Collectors.toList() );
        result.setRecords( list );
        return result;
    }

    @Override
    public List<Component> getComponentListById(Integer id) {
        Metric metric = getById( id );
        List<HeatDataDTO> dataList = druidMapper.getMetricComponentDataList( metric.getName() ,metric.getType());
        return dataList.stream().map( item->{
            Component component = new Component();
            component.setId( item.getId() );
            component.setName( item.getComponentName() );
            return component;
        }).collect( Collectors.toList() );
    }

    @Override
    public ChartResponse getDetailChart(DruidParam druidParam) {
        return componentService.druid( druidParam );
    }
}
