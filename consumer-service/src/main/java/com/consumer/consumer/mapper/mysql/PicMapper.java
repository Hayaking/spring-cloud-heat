package com.consumer.consumer.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pojo.Pic;

import java.util.List;

@Mapper
public interface PicMapper extends BaseMapper<Pic> {
    @Select("select * from pics where pid = #{pid}")
    List<Pic> searchPicsListPage(Page<Pic> pagination, @Param("pid") int pid);
}