package springboot.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用于映射Excel文件每一行数据的对象类.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDto {
    /**
     * 员工ID.
     * <p>要使用 {@code @Excel()} 注解指明其对应的行数或表头名</p>
     * <p>其余字段同理,由于该注解有中文注释其用法不再赘述</p>
     */
    @Excel(name = "员工ID", orderNum = "1")
    private Long id;
    @Excel(name = "员工姓名", orderNum = "2")
    private String name;
    /**
     * 员工性别,是否为男性.
     * <p>该字段为Boolean类型,需要添加 replace 属性使用特定的字符串声明映射规则</p>
     */
    @Excel(name = "员工性别", orderNum = "3", replace = {"男_true", "女_false"})
    private Boolean isMale;
    @Excel(name = "员工年龄", orderNum = "4")
    private Integer age;
    /**
     * 词条创建日期.
     * <p>同理,要使用 exportFormat 属性声明使用何种格式读取和写出日期时间</p>
     */
    @Excel(name = "词条创建日期", orderNum = "5", width = 20, exportFormat = "yyyy年MM月dd日")
    private Date createDate;
    @Excel(name = "员工评价", orderNum = "6")
    private String evaluate;
}
