package com.haya.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
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
@Component
public interface DbbakMapper extends BaseMapper<Dbbak> {
    @Results(id = "$user",value = {
            @Result(column = "id", property = "id"),
            @Result(column = "path", property = "path"),
            @Result(column = "createTime", property = "createTime"),
            @Result(column = "uid", property = "name", one = @One(select = "cadc.mapper.UserMapper.getNameById"))
    })
    @Select( "select * from dbbak" )
    List<Dbbak> getListWithUserName(Page<Dbbak> page);

}
