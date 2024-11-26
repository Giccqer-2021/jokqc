package springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应表 company_table 的实体类.
 * <p>本类中所说的匹配是指表名或表中字段名以小写下划线相连的方式命名且与类名或变量名的驼峰命名法相互映射的一种状态,
 * Mybatis-Plus能自动识别这一点,因此无需在 application.yml 文件中另行配置</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("company_table") // @TableName 注解表明了该实体类对应的数据表,可以省略(前提是表名与类名能够匹配)
public class CompanyTable {
    /**
     * 主键.
     * <p>注解 {@code @TableId} 用来手动声明主键字段,对应的数据表字段,以及主键的自动填充方式.
     * {@code IdType.AUTO} 表明主键为自增型主键,默认情况下,如果不加该注解则Mybatis-Plus会将变量名与
     * 表中字段名相互匹配且为 id 或 userId 的 Integer 或 Long 类型的字段作为主键,其默认自增方式为
     * {@code IdType.ASSIGN_ID} 自带的雪花算法
     */
    @TableId(value = "employee_id", type = IdType.AUTO)
    private Integer employeeId;
    /**
     * 员工姓名.
     * <p> {@code @TableField} 在表字段名与类名不匹配的时候可以用这个注解声明</p>
     */
    @TableField(value = "employee_name")
    private String employeeName;
    private Boolean isMale;
    private Long employeePhone;
    private String notes;

    /*
      @TableField 注解表明该字段为标记数据是否已经被逻辑删除的标识字段,默认为0表示未删除,1表示已删除
      被逻辑删除的数据不会真正从数据库中删除,但在使用查询方法时会将这些数据排除在外.
      数据库中要有对应字段方可生效
     */
//    @TableField(value = "is_delete")
//    private Integer isDelete;
     /*
       @Version 注解表明该字段为版本号字段,每次更新时会自动匹配并更新该字段,以防止多个用户操作同一数据时发生冲突
       数据库中要有对应字段方可生效
     */
//    @Version
//    private Integer version;
}
