package mybatis.mapper;

import mybatis.entity.EmployeeTable;

import java.util.List;

/**
 * 对应数据表 employee_table ,以及与之有外键关联的 department_table .
 */
public interface EmployeeTableMapper {
    /**
     * 联表查询.
     *
     * @return 联表查询的结果
     */
    List<EmployeeTable> getAllEmployees();
}