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
@TableName("user")
public class User extends Model<User> {
    @TableId(type = IdType.AUTO)
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Integer roleId;
    private Date createTime;

    @TableField(exist = false)
    private Role role;
}
