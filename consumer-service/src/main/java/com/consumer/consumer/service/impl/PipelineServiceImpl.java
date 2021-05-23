package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.bean.vo.StationVO;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.mapper.mysql.ComponentMapper;
import com.consumer.consumer.mapper.mysql.PipelineMapper;
import com.consumer.consumer.mapper.mysql.StationMapper;
import com.consumer.consumer.service.PipelineService;
import com.consumer.consumer.util.DoubleFormatUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pojo.Component;
import pojo.Pipeline;
import pojo.Station;

import java.util.*;
import java.util.stream.Collectors;

import static com.consumer.consumer.bean.enu.MetricEnum.*;

@Service
public class PipelineServiceImpl extends ServiceImpl<PipelineMapper, Pipeline> implements PipelineService {

    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private DruidMapper druidMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String[] TYPE_ARRAY = {"","温度传感器", "流量传感器", "压力传感器", "阀门传感器", "泵传感器"};


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean savePipeline(PipelineVo pipeline) {
        Integer id = pipeline.getId();
        if (id != null) {
            List<Integer> existComponentIdList = componentMapper.getIdListByPipeId( id );
            componentMapper.updatePipeId( existComponentIdList, null );
            List<Integer> sensorIdList = pipeline.getSensorIdList();
            if (!CollectionUtils.isEmpty( sensorIdList )) {
                componentMapper.updatePipeId( sensorIdList, id );
            }
        }
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
            PipelineVo vo = new PipelineVo();
            BeanUtils.copyProperties( item, vo );
            List<Integer> sensorIdList = componentMapper.getIdListByPipeId( item.getId() );
            List<HeatDataDTO> dataList = druidMapper.getPipeLineBaseInfo( sensorIdList );
            if (!CollectionUtils.isEmpty( dataList )) {
                Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
                list.forEach( (metricName, metricValues) -> {
                    HeatDataDTO data = metricValues.get( 0 );
                    Double metricValue = data.getMetricValue();
                    if (pipeline_water_temperature.name().equals( metricName )) {
                        vo.setTemperature( DoubleFormatUtil.halfUp( metricValue ) );
                    } else if (pipeline_water_flow.name().equals( metricName )) {
                        vo.setFlow( DoubleFormatUtil.halfUp( metricValue ) );
                    } else if (pipeline_water_pressure.name().equals( metricName )) {
                        vo.setPressure( DoubleFormatUtil.halfUp( metricValue ) );
                    } else if (pipeline_water_level.name().equals( metricName )) {
                        vo.setLevel( DoubleFormatUtil.halfUp( metricValue ) );
                    }
                } );
            }
            vo.setSensorIdList( sensorIdList );
            return vo;
        } ).collect( Collectors.toList() );
        result.setRecords( voList );
        result.setCurrent( page.getCurrent() );
        result.setTotal( page.getTotal() );
        result.setSize( page.getSize() );
        return result;
    }


    @Override
    public ChartResponse druid(DruidParam druidParam) {
        if ("sensor_pie".equals( druidParam.getMetricName() )) {
            return druidSensorPieChart(druidParam);
        }
        return druidChart( druidParam );
    }


    public ChartResponse druidChart(DruidParam druidParam) {
        ChartResponse chartResponse = new ChartResponse();
        int pipeLineId = druidParam.getId();
        List<Integer> sensorIdList = componentMapper.getIdListByPipeId( pipeLineId );
        List<HeatDataDTO> dataList = druidMapper.getPipeLineChart( sensorIdList, druidParam );
        dataList.stream()
                .collect( Collectors.groupingBy( HeatDataDTO::getId ) )
                .forEach( (id, list) -> {
                    Optional<HeatDataDTO> first = list.stream().findFirst();
                    ChartResponse.Serie serie = chartResponse.addSerie( first.get().getComponentName(), ChartResponse.SerieType.line );
                    list.forEach( data -> serie.addData(
                            data.get__time().getTime(),
                            DoubleFormatUtil.halfUp( data.getMetricValue()
                            ) ) );
                } );
        return chartResponse;
    }

    public ChartResponse druidSensorPieChart(DruidParam druidParam) {
        ChartResponse chartResponse = new ChartResponse();
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "pipe_id", druidParam.getId() );
        List<Component> componentList = componentMapper.selectList( wrapper );
        Map<Integer, List<Component>> typeMap = componentList.stream().collect( Collectors.groupingBy( Component::getType ) );
        ChartResponse.Serie serie = chartResponse.addSerie( "传感器占比", ChartResponse.SerieType.pie );
        typeMap.forEach( (type, list) -> serie.addData( TYPE_ARRAY[type], Double.valueOf( list.size() ) ) );
        return chartResponse;
    }

    @Override
    public PipelineVo getBaseInfo(Integer id) {
        Pipeline pipeline = getById( id );
        PipelineVo vo = new PipelineVo();
        BeanUtils.copyProperties( pipeline, vo );
        List<Integer> sensorIdList = componentMapper.getIdListByPipeId( id );
        List<HeatDataDTO> dataList = druidMapper.getPipeLineBaseInfo( sensorIdList );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                HeatDataDTO data = metricValues.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pipeline_water_temperature.name().equals( metricName )) {
                    vo.setTemperature( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_water_flow.name().equals( metricName )) {
                    vo.setFlow( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_water_pressure.name().equals( metricName )) {
                    vo.setPressure( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_water_level.name().equals( metricName )) {
                    vo.setLevel( DoubleFormatUtil.halfUp( metricValue ) );
                }
            } );
        }
        return vo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<StationVO> getStationList(Integer id) {
        List<StationVO> result = new LinkedList<>();
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "pipe_id", id );
        wrapper.isNotNull( "station_id" );
        List<Component> componentList = componentMapper.selectList( wrapper );
        if (CollectionUtils.isEmpty( componentList )) {
            return result;
        }
        List<Integer> stationIdList = componentList.stream()
                .map( Component::getStationId )
                .distinct()
                .collect( Collectors.toList() );
        List<Station> stations = stationMapper.selectBatchIds( stationIdList );
        if (CollectionUtils.isEmpty( stations )) {
            return result;
        }
        result = stations.stream().map( item -> {
            StationVO vo = new StationVO();
            BeanUtils.copyProperties( item, vo );
            return vo;
        } ).collect( Collectors.toList() );
        return result;
    }

    @Override
    public Page<ComponentVO> getSensorList(Page<ComponentVO> page, Integer id) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "pipe_id", id );
        Page<Component> componentPage = componentMapper.selectPage( new Page<>( page.getCurrent(), page.getSize() ), wrapper );
        List<ComponentVO> result = componentPage.getRecords().stream()
                .map( item -> {
                    ComponentVO vo = new ComponentVO();
                    BeanUtils.copyProperties( item, vo );
                    String up = redisTemplate.opsForValue().get( "component:up:" + item.getId() );
                    if (StringUtils.isEmpty( up )) {
                        vo.setUp( 2 );
                    } else {
                        vo.setUp( Integer.valueOf( up ) );
                    }
                    return vo;
                } ).collect( Collectors.toList() );
        page.setRecords( result );
        page.setTotal( componentPage.getTotal() );
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatch(List<Integer> idList) {
        idList.forEach( id->{
            List<Integer> sensorIdList = componentMapper.getIdListByPipeId( id );
            componentMapper.updatePipeId( sensorIdList, null );
            removeById( id );
        } );
        return true;
    }

    @Override
    public boolean unbindByComponentId(Integer id) {
        return 1 == componentMapper.updatePipeId( Collections.singletonList( id ), null );
    }
}
