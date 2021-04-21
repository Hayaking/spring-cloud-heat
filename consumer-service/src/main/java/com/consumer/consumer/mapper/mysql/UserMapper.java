package com.consumer.consumer.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pojo.User;

/**
 * @author haya
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
