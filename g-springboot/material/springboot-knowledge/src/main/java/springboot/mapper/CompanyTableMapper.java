package springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import springboot.entity.CompanyTable;

import java.util.List;

/**
 * 持久层接口.
 * <p>使用{@code @Mapper}和{@code @Repository}注解标记</p>
 */
@Mapper
@Repository
public interface CompanyTableMapper {
    List<CompanyTable> getAllEmployees();//获取全部员工信息

    List<CompanyTable> getEmployeeById(int id);//根据id获取员工信息
}
