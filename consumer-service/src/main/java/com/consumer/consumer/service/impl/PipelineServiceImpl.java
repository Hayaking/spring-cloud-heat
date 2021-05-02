package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.mapper.mysql.ComponentMapper;
import com.consumer.consumer.mapper.mysql.PipelineMapper;
import com.consumer.consumer.service.PipelineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pojo.Pipeline;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PipelineServiceImpl extends ServiceImpl<PipelineMapper, Pipeline> implements PipelineService {

    @Autowired
    private ComponentMapper componentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean savePipeline(PipelineVo pipeline) {
        List<Integer> sensorIdList = pipeline.getSensorIdList();
        Integer id = pipeline.getId();
        componentMapper.updatePipeId( sensorIdList, id );
        Pipeline pipe = new Pipeline();
        BeanUtils.copyProperties( pipeline, pipe );
        return saveOrUpdate( pipe );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PipelineVo> getPage(Integer pageNum, Integer pageSize, String key) {
        QueryWrapper<Pipeline> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty( key )) {
            wrapper.like( "name", key );
        }
        Page<Pipeline> page = page( new Page<>( pageNum, pageSize ), wrapper );
        Page<PipelineVo> result = new Page<>();
        List<Pipeline> records = page.getRecords();
        List<PipelineVo> voList = records.stream().map( item -> {
            List<Integer> sensorIdList = componentMapper.getIdListByPipeId( item.getId() );
            PipelineVo vo = new PipelineVo();
            BeanUtils.copyProperties( item, vo );
            vo.setSensorIdList( sensorIdList );
            return vo;
        } ).collect( Collectors.toList() );
        result.setRecords( voList );
        result.setCurrent( page.getCurrent() );
        result.setTotal( page.getTotal() );
        result.setSize( page.getSize() );
        return result;
    }
}
