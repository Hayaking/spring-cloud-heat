package pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("alarm")
public class Alarm extends Model<Alarm> implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer componentId;
    private Integer level;
    private Integer configId;
    private Integer metricId;
    private String metricName;
    private Double metricValue;

    private Date ctime;
}
