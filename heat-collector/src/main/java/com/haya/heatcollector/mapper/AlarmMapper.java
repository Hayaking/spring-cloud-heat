package com.haya.heatcollector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haya.heatcollector.entity.Alarm;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author haya
 */
@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {
}
