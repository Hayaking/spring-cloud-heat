package com.consumer.consumer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import pojo.Consumer;
import pojo.ConsumerConfig;

import java.util.List;

/**
 * @author haya
 */
@Mapper
public interface ConsumerConfigMapper extends BaseMapper<ConsumerConfig> {

    @Results(id = "$consumer", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "gprs_id", property = "gprsId"),
            @Result(column = "temp_upper", property = "tempUpper"),
            @Result(column = "temp_lower", property = "tempLower"),
            @Result(column = "pres_upper", property = "presUpper"),
            @Result(column = "pres_lower", property = "presLower"),
            @Result(column = "flow_upper", property = "flowUpper"),
            @Result(column = "flow_lower", property = "flowLower"),
            @Result(column = "create_date", property = "createDate"),
            @Result(column = "lng", property = "lng"),
            @Result(column = "lat", property = "lat"),
            @Result(column = "consumer_id", property = "consumerId"),
            @Result(column = "consumer_id", property = "consumer", one = @One(select = "com.consumer.consumer.mapper.ConsumerMapper.getById"))
    })
    @Select(value = "select * from consumer_config")
    List<ConsumerConfig> getListWithConsumer();
}
