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
@TableName("pipeline")
public class Pipeline extends Model<Pipeline> implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String path;
    private String name;
    private String color;
    private Integer type;
    private Double length;
    private Date createDate;
}
