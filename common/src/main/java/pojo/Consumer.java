package pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("consumer")
public class Consumer extends Model<Consumer> {
    @TableId(type = IdType.AUTO)
    private String id;
    private String name;
    private String address;
    private Date createDate;
    private Integer configId;
    private Integer creatorId;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
    @TableField(exist = false)
    private User creator;
    @TableField(exist = false)
    private ConsumerConfig config;
}
