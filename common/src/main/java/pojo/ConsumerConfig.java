package pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author haya
 */
//@EqualsAndHashCode(callSuper = true)
@Data
//@ToString()
@TableName("consumer_config")
public class ConsumerConfig extends Model<ConsumerConfig> {
    @TableId(type = IdType.AUTO)
    private String id;
    private Integer gprsId;
    private BigDecimal tempUpper;
    private BigDecimal tempLower;
    private BigDecimal presUpper;
    private BigDecimal presLower;
    private BigDecimal flowUpper;
    private BigDecimal flowLower;
    private Date createDate;
    private Float lng;
    private Float lat;
    private Integer consumerId;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;

    @TableField(exist = false)
    private Grps grps;
    @TableField(exist = false)
    private Consumer consumer;
}
