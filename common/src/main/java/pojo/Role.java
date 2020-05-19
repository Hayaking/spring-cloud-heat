package pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author haya
 */
//@EqualsAndHashCode(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("role")
public class Role extends Model<Role> {
    private int id;
    private String name;
    @TableLogic
    private Integer deleted;
    @Version
    private Integer version;
}
