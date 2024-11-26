package springboot.entity;

import lombok.Data;

/**
 * 数据表 company 对应的 entity.
 * <p>注意：在mysql数据库中，所有的属性名称的写法都是 小写横线相连 式的写法</p>
 * <p>如 employeeId 在数据库中的写法为 employee_id ，employeeName 写法为 employee_name</p>
 * <P>为了让程序能正确识别二者的映射关系,需要在 application.yml 中写入如下配置:
 * <blockquote><pre>
 * mybatis:
 *   configuration:
 *     map-underscore-to-camel-case: true
 * </pre></blockquote>
 * </P>
 */
@Data
public class CompanyTable {
    private Integer employeeId;
    private String employeeName;
    private Boolean isMale;
    private Long employeePhone;
    private String notes;
}
