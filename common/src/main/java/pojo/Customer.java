package pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("customer")
public class Customer extends Model<Customer> {
    @TableId(type = IdType.AUTO)
    private String id;
    private String gprsId;
    private Integer addr;
    private String custName;
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
    private Boolean online;

}
