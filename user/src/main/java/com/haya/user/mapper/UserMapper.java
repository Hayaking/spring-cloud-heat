package com.haya.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pojo.User;

/**
 * @author haya
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
