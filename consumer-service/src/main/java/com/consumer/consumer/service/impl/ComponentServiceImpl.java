package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.enu.MetricEnum;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.HeatMapData;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.mapper.mysql.ComponentMapper;
import com.consumer.consumer.mapper.mysql.PicMapper;
import com.consumer.consumer.mapper.mysql.StationMapper;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.util.DoubleFormatUtil;
import com.consumer.consumer.util.MapUtil;
import com.consumer.consumer.util.RedisLock;
import com.consumer.consumer.util.WrapperUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pojo.Component;
import pojo.Pic;
import pojo.Station;

import java.util.*;
import java.util.stream.Collectors;

import static com.consumer.consumer.bean.enu.MetricEnum.*;
import static java.util.Arrays.asList;

@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {
    private static final Map<Integer, String> childTypeMap = new HashMap<Integer, String>() {{
        put( 1, "一次供水管道" );
        put( 2, "一次回水管道" );
        put( 3, "二次供水管道" );
        put( 4, "二次回水管道" );
        put( 5, "循环管道" );
    }};
    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private DruidMapper druidMapper;
    @Autowired
    private PicMapper picMapper;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void drawChart() {

    }

    @Override
    public Page<ComponentVO> getComponentPage(ComponentFilter filter) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "type", filter.getType() );
        WrapperUtil.buildCondition( filter, wrapper );
        Page<Component> page = new Page<>( filter.getPageNum(), filter.getPageSize() );
        Page<Component> componentPage = componentMapper.selectPage( page, wrapper );
        Page<ComponentVO> resultPage = new Page<ComponentVO>() {{
            setTotal( componentPage.getTotal() );
            setCurrent( componentPage.getCurrent() );
            setSize( componentPage.getSize() );
            setPages( componentPage.getPages() );
        }};
        List<Component> componentList = componentPage.getRecords();
        List<Integer> ids = componentList
                .stream()
                .map( Component::getId )
                .collect( Collectors.toList() );
        // 查druid
        List<HeatDataDTO> dataList = druidMapper.selectByComponentIdList( ids, filter.getType() );
        Map<Integer, List<HeatDataDTO>> dataMap = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getId ) );
        List<ComponentVO> resultList = componentList.stream()
                .map( component -> {
                    ComponentVO vo = new ComponentVO();
                    BeanUtils.copyProperties( component, vo );
                    vo.setAliasName( StringUtils.isEmpty( component.getName() ) ? "--" : component.getName() );
                    String up = redisTemplate.opsForValue().get( "component:up:" + component.getId() );
                    vo.setUp( StringUtils.isEmpty( up ) ? 2 : Integer.parseInt( up ) );
                    List<HeatDataDTO> metricDataList = dataMap.get( component.getId() );
                    if (!CollectionUtils.isEmpty( metricDataList )) {
                        List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricDataList );
                        values.forEach( data -> {
                            String metricName = data.getMetricName();
                            Double metricValue = data.getMetricValue();
                            if (pipeline_water_flow.name().equals( metricName )) {
                                vo.setFlow( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_water_temperature.name().equals( metricName )) {
                                vo.setTemperature( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_water_temperature_increase.name().equals( metricName )) {
                                vo.setTemperatureSpeed( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_out_temperature.name().equals( metricName )) {
                                vo.setTemperatureOut( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_water_pressure.name().equals( metricName )) {
                                vo.setPressure( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_water_pressure_increase.name().equals( metricName )) {
                                vo.setPressureSpeed( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (station_water_temperature.name().equals( metricName )) {
                                vo.setTemperature( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (station_water_flow.name().equals( metricName )) {
                                vo.setFlow( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_water_tassels.name().equals( metricName )) {
                                vo.setFlowSpeed( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (station_water_pressure.name().equals( metricName )) {
                                vo.setPressure( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pump_watt.name().equals( metricName )) {
                                vo.setWatt( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pump_voltage.name().equals( metricName )) {
                                vo.setVoltage( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pump_state.name().equals( metricName )) {
                                vo.setState( metricValue.intValue() );
                            } else if (pipeline_water_level.name().equals( metricName )) {
                                vo.setWaterLevel( DoubleFormatUtil.halfUp( metricValue ) );
                            } else if (pipeline_valve_state.name().equals( metricName )) {
                                vo.setState( metricValue.intValue() );
                            }
                        } );
                    }
                    return vo;
                } ).collect( Collectors.toList() );
        resultPage.setRecords( resultList );
        return resultPage;
    }

    @Override
    public ComponentBaseInfoVO getBaseInfo(Integer id) {
        ComponentBaseInfoVO baseInfo = new ComponentBaseInfoVO();
        Component component = componentMapper.selectById( id );
        Integer type = component.getType();
        BeanUtils.copyProperties( component, baseInfo );
        switch (type) {
            default:
                return baseInfo;
            case 1:
                setTempBaseInfo( id, baseInfo );
                break;
            case 2:
                setFlowBaseInfo( id, baseInfo );
                break;
            case 3:
                setPressureBaseInfo( id, baseInfo );
                break;
            case 4:
                setValveBaseInfo( id, baseInfo );
                break;
            case 5:
                setPumpBaseInfo( id, baseInfo );
                break;
        }

        // 设置up
        String up = redisTemplate.opsForValue().get( "component:up:" + component.getId() );
        baseInfo.setUp( StringUtils.isEmpty( up ) ? 0 : Integer.parseInt( up ) );
        // 设置图片
        QueryWrapper<Pic> wrapper = new QueryWrapper<>();
        wrapper.eq( "pid", id );
        List<Pic> pics = picMapper.selectList( wrapper );
        baseInfo.setPicList( pics.stream().map( Pic::getPath ).collect( Collectors.toList() ) );
        return baseInfo;
    }

    private void setPumpBaseInfo(Integer id, ComponentBaseInfoVO baseInfo) {
        List<HeatDataDTO> dataList = druidMapper.gePumpSensorBaseInfo( id );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricValues );
                HeatDataDTO data = values.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pump_voltage.name().equals( metricName )) {
                    baseInfo.setVoltage( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pump_watt.name().equals( metricName )) {
                    baseInfo.setWatt( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_e_quantity.name().equals( metricName )) {
                    baseInfo.setEQuantity( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_up_time.name().equals( metricName )) {
                    baseInfo.setUpTime( metricValue.longValue() );
                }
            } );
        }
    }

    private void setValveBaseInfo(Integer id, ComponentBaseInfoVO baseInfo) {
        List<HeatDataDTO> dataList = druidMapper.getValveSensorBaseInfo( id );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricValues );
                HeatDataDTO data = values.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pipeline_valve_state.name().equals( metricName )) {
                    baseInfo.setValve( metricValue.longValue() );
                } else if (sensor_e_quantity.name().equals( metricName )) {
                    baseInfo.setEQuantity( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_up_time.name().equals( metricName )) {
                    baseInfo.setUpTime( metricValue.longValue() );
                }
            } );
        }
    }

    private void setPressureBaseInfo(Integer id, ComponentBaseInfoVO baseInfo) {
        List<HeatDataDTO> dataList = druidMapper.getPressureSensorBaseInfo( id );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricValues );
                HeatDataDTO data = values.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pipeline_water_pressure_increase.name().equals( metricName )) {
                    baseInfo.setPressure( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_water_pressure.name().equals( metricName )) {
                    baseInfo.setPressureIncrease( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_e_quantity.name().equals( metricName )) {
                    baseInfo.setEQuantity( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_up_time.name().equals( metricName )) {
                    baseInfo.setUpTime( metricValue.longValue() );
                }
            } );
        }
    }

    private void setFlowBaseInfo(Integer id, ComponentBaseInfoVO baseInfo) {
        List<HeatDataDTO> dataList = druidMapper.getFlowSensorBaseInfo( id );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricValues );
                HeatDataDTO data = values.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pipeline_water_flow.name().equals( metricName )) {
                    baseInfo.setFlow( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_water_level.name().equals( metricName )) {
                    baseInfo.setLevel( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_e_quantity.name().equals( metricName )) {
                    baseInfo.setEQuantity( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_up_time.name().equals( metricName )) {
                    baseInfo.setUpTime( metricValue.longValue() );
                }
            } );
        }
    }

    private void setTempBaseInfo(Integer id, ComponentBaseInfoVO baseInfo) {
        List<HeatDataDTO> dataList = druidMapper.getTempSensorBaseInfo( id );
        if (!CollectionUtils.isEmpty( dataList )) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getMetricName ) );
            list.forEach( (metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric( metricValues );
                HeatDataDTO data = values.get( 0 );
                Double metricValue = data.getMetricValue();
                if (pipeline_water_temperature.name().equals( metricName )) {
                    baseInfo.setTemperature( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (pipeline_out_temperature.name().equals( metricName )) {
                    baseInfo.setTemperatureOut( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_e_quantity.name().equals( metricName )) {
                    baseInfo.setEQuantity( DoubleFormatUtil.halfUp( metricValue ) );
                } else if (sensor_up_time.name().equals( metricName )) {
                    baseInfo.setUpTime( metricValue.longValue() );
                }
            } );
        }
    }

    @Override
    public ChartResponse druid(DruidParam druidParam) {
        String metricName = druidParam.getMetricName();
        if (component_up.name().equals( metricName )) {
            return drawComponentUp( druidParam );
        } else if (metricName.startsWith( "station_" )) {
            return drawStationChart( druidParam );
        } else if (pipeline_water_flow.name().equals( metricName )) {
            return draw( druidParam, null );
        } else if (pipeline_water_temperature.name().equals( metricName )) {
            return draw( druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit( "立方米/分钟" )
            ) );
        } else if (pipeline_water_temperature_increase.name().equals( metricName )) {
            return draw( druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit( "立方米/分钟" )
            ) );
        } else if (pipeline_out_temperature.name().equals( metricName )) {
            return draw( druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit( "立方米/分钟" )
            ) );
        } else {
            return draw( druidParam, null );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<HeatMapData> getHeatMap() {
        List<HeatMapData> result = new LinkedList<>();
        List<HeatDataDTO> dataList = druidMapper.getHeatMapDataList();
//        Map<Integer, List<HeatDataDTO>> dataMap = dataList.stream().collect(Collectors.groupingBy(HeatDataDTO::getId));
//        List<Integer> componentIdList = dataList.stream().map(HeatDataDTO::getId).collect(Collectors.toList());
//        List<Component> componentList = componentMapper.selectBatchIds(componentIdList);
//        for (Component component : componentList) {
//            Integer nextId = component.getNextId();
//            if (nextId == null) {
//                continue;
//            }
//            Component next = componentMapper.selectById(nextId);
//            if (next == null) {
//                continue;
//            }
//            List<HeatDataDTO> valueList = dataMap.get(component.getId());
//            List<HeatDataDTO> values = MapUtil.getLastTimeMetric(valueList);
//            HeatDataDTO data = values.get(0);
//            result.addAll(LineUtil.addPoint(
//                    component.getLat(), component.getLon(),
//                    next.getLat(), next.getLon(),
//                    data.getMetricValue()
//            ));
//        }
//        return result;
        return dataList.stream().map( item -> {
            HeatMapData data = new HeatMapData();
            data.setLat( item.getLat() );
            data.setLng( item.getLon() );
            data.setCount( item.getMetricValue() );
            return data;
        } ).collect( Collectors.toList() );
    }

    @Override
    public Double sumHourFlow() {
        HeatDataDTO data = druidMapper.selectSumHourFlow();
        if (data != null) {
            return DoubleFormatUtil.halfUp( data.getMetricValue() );
        }
        return null;
    }

    @Override
    public List getTop5(String metric) {
        return druidMapper.selectTop5( metric ).stream().map( item -> {
            item.setMetricValue( DoubleFormatUtil.halfUp( item.getMetricValue() ) );
            return item;
        } ).collect( Collectors.toList() );
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComponentVO> getHomeComponentList(String name) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty( name )) {
            return componentMapper.likeByName( name );
        }

        List<Component> componentList = componentMapper.selectList( wrapper );
        LinkedList<ComponentVO> result = componentList.stream()
                .map( Component::getStationId )
                .filter( stationId -> !StringUtils.isEmpty( stationId ) )
                .distinct()
                .map( stationId -> {
                    ComponentVO component = new ComponentVO();
                    Station station = stationMapper.selectById( stationId );
                    component.setId( stationId );
                    component.setIsStation( true );
                    component.setName( station.getName() );
                    component.setLat( station.getLat() );
                    component.setLon( station.getLon() );
                    return component;
                } )
                .collect( Collectors.toCollection( LinkedList::new ) );
        componentList.stream()
                .filter( item -> StringUtils.isEmpty( item.getStationId() ) )
                .forEach( item -> {
                    ComponentVO vo = new ComponentVO();
                    BeanUtils.copyProperties( item, vo );
                    result.add( vo );
                } );
        return result;
    }

    @Override
    public Boolean updateComponent(ComponentVO componentVO) {
        UpdateWrapper<Component> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", componentVO.getId());
        wrapper.set("name", componentVO.getName());
        wrapper.set("note", componentVO.getNote());
        wrapper.set("area", componentVO.getArea());
        wrapper.set("street", componentVO.getStreet());
        wrapper.set("pipe_id", componentVO.getPipeId());
        boolean isUpdate = update(wrapper);
        String lockKey = "lock:" + componentVO.getType() + ":" + componentVO.getLon() + ":" + componentVO.getLat();
        String uuid = UUID.randomUUID().toString();
        try {
            RedisLock.tryLock( lockKey, uuid, 5, 10 );
            redisTemplate.delete( "component:" + componentVO.getType() + ":" + componentVO.getLon() + ":" + componentVO.getLat() );
        } catch (Exception ignored) {
            return false;
        } finally {
            RedisLock.releaseLock( lockKey, uuid );
        }
        return isUpdate;
    }

    private ChartResponse drawStationChart(DruidParam druidParam) {
        ChartResponse response = new ChartResponse();
        List<HeatDataDTO> dataList = druidMapper.getStationChartDataList( druidParam );
        Map<Integer, List<HeatDataDTO>> dataMap = dataList.stream().collect( Collectors.groupingBy( HeatDataDTO::getChildType ) );
        dataMap.forEach( (childType, list) -> {
            ChartResponse.Serie serie = response.addSerie( childTypeMap.get( childType ), ChartResponse.SerieType.line );
            list.forEach( data -> serie.addData( data.get__time().getTime(), DoubleFormatUtil.halfUp( data.getMetricValue() ) ) );
        } );
        return response;
    }

    private ChartResponse drawComponentUp(DruidParam druidParam) {
        ChartResponse response = new ChartResponse();
        response.setYAxis( asList( ChartResponse.YAxis.addTickUnit( "" ).setMax( 1L ) ) );
        List<HeatDataDTO> dataList = druidMapper.getComponentUP( druidParam );
        ChartResponse.Serie serie = response.addSerie( MetricEnum.getMetricChName( druidParam.getMetricName() ), ChartResponse.SerieType.column );
        HashMap<Double, String> colorMap = new HashMap<Double, String>() {{
            put( 0d, "#07F531" );
            put( 1d, "#FFF000" );
            put( 2d, "#FF0000" );
        }};
        dataList.forEach( data -> {
            serie.addData(
                    data.get__time().getTime(),
                    1D,
                    colorMap.get( data.getMetricValue() )
            );
        } );
        return response;
    }

    // 管道流量
    private ChartResponse draw(DruidParam druidParam, List<ChartResponse.YAxis> units) {
        ChartResponse response = new ChartResponse();
        response.setYAxis( units );
        List<HeatDataDTO> dataList = druidMapper.getPipeLineWaterFlow( druidParam );
        ChartResponse.Serie serie = response.addSerie( MetricEnum.getMetricChName( druidParam.getMetricName() ), ChartResponse.SerieType.line );
        dataList.forEach( data -> serie.addData( data.get__time().getTime(), DoubleFormatUtil.halfUp( data.getMetricValue() ) ) );
        return response;
    }
}
