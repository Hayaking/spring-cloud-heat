package com.consumer.consumer.service.impl;

import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.vo.CollectorVO;
import com.consumer.consumer.bean.vo.SensorVO;
import com.consumer.consumer.mapper.phoenix.PhoenixMapper;
import com.consumer.consumer.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectorServiceImpl implements CollectorService {
    @Autowired
    private PhoenixMapper phoenixMapper;


    @Override
    public List<CollectorVO> getPage(ComponentFilter filter) {
        String name = null;
        List<HeatDataDTO> collectorList = phoenixMapper.getCollectorList( name);
        List<CollectorVO> result = new LinkedList<>();
        if (CollectionUtils.isEmpty( collectorList )) {
            return result;
        }

        collectorList.stream()
                .collect( Collectors.groupingBy( HeatDataDTO::getCollectorName ) )
                .forEach( (collectorName, list) -> {
                    CollectorVO vo = new CollectorVO();
                    vo.setName( collectorName );
                    List<SensorVO> sensorList = list.stream().map( item -> {
                        SensorVO sensorVO = new SensorVO();
                        sensorVO.setId( item.getId() );
                        sensorVO.setName( item.getComponentName() );
                        sensorVO.setTime( item.get__time() );
                        return sensorVO;
                    } ).collect( Collectors.toList() );
                    vo.setTime( sensorList.get( 0 ).getTime() );
                    vo.setSensorList( sensorList );
                    result.add( vo );
                } );
        return result;
    }
}
