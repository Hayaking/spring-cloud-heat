package pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("grps")
public class Grps extends Model<Grps> {
    private int id;
    private String no;
    private String des;
    private boolean isOnline;
    private Date createDate;

    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}
