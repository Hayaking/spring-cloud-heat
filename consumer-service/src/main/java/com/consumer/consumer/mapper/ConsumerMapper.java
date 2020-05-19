package com.consumer.consumer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pojo.Consumer;
import pojo.Role;

/**
 * @author haya
 */
@Mapper
public interface ConsumerMapper extends BaseMapper<Consumer> {

    @Select( value = "select * from consumer where id = #{id}")
    Consumer getById(@Param( "id" ) int id);
}
