package pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class Grps extends Model<Grps> {
    private int id;
    private String no;
    private String describe;
    private boolean isOnline;
    private Long createDate;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}
