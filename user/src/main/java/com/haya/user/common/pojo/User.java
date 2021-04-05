package com.haya.user.common.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user")
public class User extends Model<User> {
    @TableId(type = IdType.AUTO)
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer roleId;
    private Date createDate;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    @TableField(exist = false)
    private Role role;
}
