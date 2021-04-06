package com.consumer.consumer.mapper.druid;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.consumer.consumer.bean.HeatDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author haya
 */
@Mapper
public interface MinuteMapper extends BaseMapper<HeatDataDTO> {
    List<HeatDataDTO> test();
}
