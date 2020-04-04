package pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customer")
public class CustomerConfig extends Model<CustomerConfig> {
    @TableId(type = IdType.AUTO)
    private String id;
    private String gprsId;
    private BigDecimal tempRange;
    private BigDecimal tempUpper;
    private BigDecimal tempLower;
    private BigDecimal presRange;
    private BigDecimal presUpper;
    private BigDecimal presLower;
    private BigDecimal flowRange;
    private BigDecimal flowUpper;
    private BigDecimal flowLower;
    private BigDecimal flowMulti;
    private Long createTime;
    @TableField(exist = false)
    private Grps grps;
}
