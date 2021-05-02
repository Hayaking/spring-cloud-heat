package com.consumer.consumer.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pojo.Component;

import java.util.List;

@Mapper
public interface ComponentMapper extends BaseMapper<Component> {

    int updatePipeId(@Param("idList") List<Integer> idList, @Param("pipeId") int pipeId);

    List<Integer> getIdListByPipeId(@Param("pipeId") int pipeId);
}
