package mybatis.entity;

import lombok.Data;

/**
 * 对应数据表 employee_table 的实体类,该表与 department_table 存在外检关联.
 * <p>数据表字段 employee_department_id 并没有作为本类的属性</p>
 */
@Data
public class EmployeeTable {
    private Integer employeeId;
    private String employeeName;
    /**
     * 需要联表查询的对象字段,该对象封装另一个数据表的数据.
     */
    private DepartmentTable employeeDepartment;
}
