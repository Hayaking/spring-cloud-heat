package com.haya.heatcollector.entity;

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
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer roleId;
    private Date ctime;
    private Date mtime;
    private Boolean enable;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}
