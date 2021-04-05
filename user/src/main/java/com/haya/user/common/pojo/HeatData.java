package com.haya.user.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author haya
 */
@Data
@TableName("heatdata")
public class HeatData  {
	@TableId
	private String id;
	private Integer consumerId;
	private double temperature;
	private double pressure;
	private double flow;
	private long createDate;
}
