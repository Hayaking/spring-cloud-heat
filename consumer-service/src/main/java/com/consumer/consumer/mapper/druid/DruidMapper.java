package com.consumer.consumer.mapper.druid;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haya
 */
@Mapper
public interface DruidMapper extends BaseMapper<HeatDataDTO> {
    List<HeatDataDTO> test();

    List<HeatDataDTO> selectByComponentIdList(@Param("idList") List<Integer> idList,
                                              @Param("type") Integer type);

    List<HeatDataDTO> getPipeLineWaterFlow(@Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getPipeLineBaseInfo(@Param("idList")  List<Integer> idList);

    List<HeatDataDTO> getComponentUP(@Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getStationChartDataList(@Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getPumpBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getStationBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getHeatMapDataList();

    HeatDataDTO selectSumHourFlow();

    List<HeatDataDTO> selectTop5(@Param("metric") String metric);

    List<HeatDataDTO>  getPipeLineChart(@Param("idList") List<Integer> sensorIdList,
                                        @Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getTempSensorBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getFlowSensorBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getPressureSensorBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getValveSensorBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> gePumpSensorBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getMetricComponentDataList(@Param("metricName") String metricName);

    List<HeatDataDTO> getCollectorList(@Param("name") String name);
}
