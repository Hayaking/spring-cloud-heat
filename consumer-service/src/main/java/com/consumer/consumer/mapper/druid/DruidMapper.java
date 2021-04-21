package com.consumer.consumer.mapper.druid;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
                                              @Param("type") Integer[] type);

    List<HeatDataDTO> getPipeLineWaterFlow(@Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getPipeLineBaseInfo(@Param("id") Integer id);

    List<HeatDataDTO> getComponentUP(@Param("druidParam") DruidParam druidParam);

    List<HeatDataDTO> getStationChartDataList(@Param("druidParam") DruidParam druidParam);
}
