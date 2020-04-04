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
public class Customer extends Model<Customer> {
    @TableId(type = IdType.AUTO)
    private String id;
    private String name;
    private String address;
    private Long createTime;
    private Integer configId;
    private Integer creatorId;
    @TableField(exist = false)
    private User creator;
    @TableField(exist = false)
    private CustomerConfig config;
}
