package com.security.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import pojo.Dbbak;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yyp
 * @since 2020-05-27
 */
@Mapper
public interface DbbakMapper extends BaseMapper<Dbbak> {

    @Select("select * from dbbak")
    List<Dbbak> getListWithUserName(Page<Dbbak> page);

}
