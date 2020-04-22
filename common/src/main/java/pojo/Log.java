package pojo;

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
@TableName("log")
public class Log extends Model<Log> {
    @TableId
    private int id;
    private int userId;
    private String ip;
    private Date createDate;
    private String type;
    private String remarks;
    private String operation;
}
