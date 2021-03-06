package com.haya.user.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("report_day")
public class ReportDay extends Model<ReportDay> {
    @TableId
    private int id;
    private int consumerId;
    private double preTemp;
    private double prePres;
    private double preFlow;
    private double temp;
    private double pres;
    private double flow;
    private Date createDate;
}
