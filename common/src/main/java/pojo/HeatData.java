package pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("heatdata")
public class HeatData extends Model<HeatData> {
	@TableId
	private String id;
	private int addr;
	private String custName;
	private double temperature;
	private double pressure;
	private double flow;
	private double totalFlow;
	private double monthFlow;
	private double dayFlow;
	private long acquireTime;
	private long createDate;
}
