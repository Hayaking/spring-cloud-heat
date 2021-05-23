package pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("metric")
public class Metric extends Model<Metric> {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer type;
    private String unit;
    private String note;
    private String aliasName;
    private Date ctime;
}
