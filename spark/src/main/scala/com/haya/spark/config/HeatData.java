package com.haya.spark.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author haya
 */

@EqualsAndHashCode()
@Data
@Builder
public class HeatData implements Serializable {

	private Integer id;
	private Integer consumerId;
	private double temperature;
	private double pressure;
	private double flow;
	private long createDate;

}
