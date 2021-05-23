package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.StationVO;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.mapper.mysql.ComponentMapper;
import com.consumer.consumer.mapper.mysql.StationMapper;
import com.consumer.consumer.service.StationService;
import com.consumer.consumer.util.DoubleFormatUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.Component;
import pojo.Station;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private DruidMapper druidMapper;
    @Autowired
    private ComponentMapper componentMapper;

    @Override
    public Page<StationVO> getPage(Integer pageNum, Integer pageSize, String key) {
        QueryWrapper<Station> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty( key )) {
            wrapper.eq( "name", key );
        }
        Page<Station> page = page( new Page<>( pageNum, pageSize ), wrapper );
        Page<StationVO> result = new Page<>();
        List<Station> records = page.getRecords();
        List<StationVO> voList = records.stream().map( item -> {
            StationVO vo = new StationVO();
            BeanUtils.copyProperties( item, vo );
            QueryWrapper<Component> wrapper1 = new QueryWrapper<>();
            wrapper1.eq( "station_id", item.getId() );
            List<Integer> componentIdList = componentMapper.selectList( wrapper1 )
                    .stream()
                    .map( Component::getId )
                    .collect( Collectors.toList() );
            vo.setComponentIdList( componentIdList );
            return vo;
        } ).collect( Collectors.toList() );
        result.setRecords( voList );
        result.setCurrent( page.getCurrent() );
        result.setTotal( page.getTotal() );
        result.setSize( page.getSize() );
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveWithComponent(StationVO station) {
        boolean flag = saveOrUpdate( station );
        if (station.getId() != null) {
            List<Integer> existComponentIdList = componentMapper.getIdListByStationId( station.getId() );
            componentMapper.updateStationId( existComponentIdList, null );
        }
        List<Integer> componentIdList = station.getComponentIdList();
        int row = componentMapper.updateStationId( componentIdList, station.getId() );
        return flag && row != 0;
    }

    @Override
    public ChartResponse druid(DruidParam druidParam) {
        ChartResponse chartResponse = new ChartResponse();
        int pipeLineId = druidParam.getId();
        List<Integer> sensorIdList = componentMapper.getIdListByStationId( pipeLineId );
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

    @Override
    public Page<ComponentVO> getSensorList(Page<ComponentVO> page, Integer id) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "station_id", id );
        Page<Component> componentPage = componentMapper.selectPage( new Page<>( page.getCurrent(), page.getSize() ), wrapper );
        List<ComponentVO> result = componentPage.getRecords().stream()
                .map( item -> {
                    ComponentVO vo = new ComponentVO();
                    BeanUtils.copyProperties( item, vo );
                    return vo;
                } ).collect( Collectors.toList() );
        page.setRecords( result );
        page.setTotal( componentPage.getTotal() );
        return page;
    }

    @Override
    public StationVO getBaseInfo(Integer id) {
        Station station = getById( id );
        StationVO vo = new StationVO();
        BeanUtils.copyProperties( station, vo );
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.eq( "station_id",id );
        vo.setComponentList( componentMapper.selectList( wrapper ) );
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBatch(List<Integer> idList) {
        idList.forEach( id->{
            List<Integer> sensorIdList = componentMapper.getIdListByStationId( id );
            componentMapper.updateStationId( sensorIdList, null );
            removeById( id );
        } );
        return true;
    }
}
