package com.security.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import pojo.User;

/**
 * @author haya
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update user set enable = !enable where id = #{id}")
    int setUserEnable(@Param("id") Integer id);
    @Update("update user set password = #{password} where username = #{username} and email = #{email}")
    int updateByUserNameAndEmail(@Param("username") String username,
                                 @Param("email") String email,
                                 @Param("password") String password);
}
