package springboot.service;

import springboot.entity.CompanyTable;
import springboot.service.impl.CompanyServiceImpl;

import java.util.List;

/**
 * Service层接口.
 * <p>需自行书写实现类 {@code CompanyServiceImpl}</p>
 *
 * @see CompanyServiceImpl
 */
public interface CompanyService {
    List<CompanyTable> getAllEmployeesData();

    CompanyTable getTheFirstEmployee();
}
