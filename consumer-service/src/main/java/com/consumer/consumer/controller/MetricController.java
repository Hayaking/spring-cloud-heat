package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.MetricVO;
import com.consumer.consumer.service.MetricService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pojo.Component;
import pojo.Metric;

import java.util.List;

@RestController
@RequestMapping("/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;

    @GetMapping(value = "/list/{type}")
    public Message list(@PathVariable Integer type) {
        QueryWrapper<Metric> wrapper = new QueryWrapper<>();
        wrapper.eq("type", type);
        List<Metric> list = metricService.list( wrapper );
        list.forEach( item -> {
            if (StringUtils.isEmpty( item.getAliasName() )) {
                item.setAliasName( item.getName() );
            }
        } );

        return MessageFactory.message(true, list);
    }

    @PostMapping("/page")
    public Message getComponentPage(@RequestBody ComponentFilter filter) {
        Page<MetricVO> componentPage = metricService.getMetricPage(filter);
        return MessageFactory.message(true, componentPage);
    }

    @PostMapping("/save")
    public Message getComponentPage(@RequestBody MetricVO metric) {
        UpdateWrapper<Metric> wrapper = new UpdateWrapper<>();
        wrapper.eq( "id", metric.getId() );
        wrapper.set( "note", metric.getNote() )
                .set( "unit", metric.getUnit() )
                .set( "alias_name", metric.getAliasName() );
        boolean isUpdate = metricService.update( wrapper );
        return MessageFactory.message(isUpdate);
    }

    @GetMapping("/detail/component/{id}")
    public Message getDetail(@PathVariable Integer id) {
        List<Component> list = metricService.getComponentListById( id );
        return MessageFactory.message( true, list );
    }

    @PostMapping("/detail/component/chart")
    public ChartResponse getDetailChart(@RequestBody DruidParam druidParam) {
        ChartResponse chart = metricService.getDetailChart( druidParam );
        return chart;
    }

}
