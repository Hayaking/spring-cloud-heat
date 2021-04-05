package com.haya.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haya.user.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author haya
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
