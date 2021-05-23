package com.security.security.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pojo.User;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVo extends User {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mtime;
}
