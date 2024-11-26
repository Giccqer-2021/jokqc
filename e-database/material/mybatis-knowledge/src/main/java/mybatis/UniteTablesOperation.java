package mybatis;

import mybatis.mapper.EmployeeTableMapper;
import mybatis.util.MybatisUtil;

/**
 * 本方法用于测试 {@link EmployeeTableMapper} 的联表查询操作
 */
public class UniteTablesOperation {
    public static void main(String[] args) {
        MybatisUtil.operate(EmployeeTableMapper.class, mapper -> {
            System.out.println("执行联合查询操作,其结果为: ");
            mapper.getAllEmployees().forEach(System.out::println);
        }, false);
    }
}
