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
@TableName("station")
public class Station extends Model<Station> implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String area;
    private String street;
    private Double lat;
    private Double lon;
    private Date ctime;
    private Date mtime;
}
