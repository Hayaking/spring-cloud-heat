package pojo;


import com.baomidou.mybatisplus.annotation.*;
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
public class ConsumerConfig extends Model<ConsumerConfig> {
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
    private Long createDate;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    @TableField(exist = false)
    private Grps grps;
}
