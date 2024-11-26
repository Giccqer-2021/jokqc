package springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.mapper.CompanyTableMapper;
import springboot.entity.CompanyTable;
import springboot.service.CompanyService;

import java.util.List;

/**
 * service层实现类.
 * <p>使用 {@code @Service} 标记</p>
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyTableMapper companyTableMapper;

    /**
     * 获取全部员工信息.
     *
     * @return 被 {@code Company} 类封装的员工信息数组
     */
    @Override
    public List<CompanyTable> getAllEmployeesData() {
        return companyTableMapper.getAllEmployees();
    }

    /**
     * 获取第一条员工信息.
     *
     * @return 被 {@code Company} 类封装的员工信息
     */
    @Override
    public CompanyTable getTheFirstEmployee() {
        List<CompanyTable> result = companyTableMapper.getEmployeeById(1);
        if (!result.isEmpty()) return result.getFirst();
        return null;
    }
}
