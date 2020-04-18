package pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
