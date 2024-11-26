package mybatis.entity;

import lombok.Data;

/**
 * 对应数据表 department_table 的实体类.
 */
@Data
public class DepartmentTable {
    private Integer departmentId;
    private String departmentName;
}
