package com.haya.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haya.user.common.pojo.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author haya
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    @Select( value = "select * from log where create_date BETWEEN #{start} and #{end} ORDER BY create_date DESC" )
    List<Log> getPageByDateRange(Page<Log> page,
                                    @Param( "start" ) LocalDateTime start,
                                    @Param( "end" ) LocalDateTime end);

}
