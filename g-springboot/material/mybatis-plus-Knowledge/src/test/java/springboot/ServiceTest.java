package springboot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.entity.CompanyTable;
import springboot.service.CompanyService;

/**
 * 本类用于测试 MyBatis-Plus 对 service 层提供的方法支持.
 */
@SpringBootTest
public class ServiceTest {
    @Autowired
    private CompanyService companyService;

    /**
     * 输出所有数据表数据,用于测试的第一句话.
     */
    @Test
    public void connectionTest() {
        System.out.println("service循环打印数据表信息: ");
        companyService.list().forEach(System.out::println);
    }

    /**
     * 测试 service 层的 CRUD 方法.
     */
    @Test
    public void CURDTest() {
        CompanyTable insertTable = new CompanyTable(null, "大尾巴", true, null, "阳光彩虹小白马");
        companyService.save(insertTable); //根据实体类对象插入数据
        QueryWrapper<CompanyTable> newTableWrapper = new QueryWrapper<>();
        newTableWrapper.eq("employee_name", "大尾巴");
        CompanyTable resultTable = companyService.getOne(newTableWrapper);
        System.out.println("新增后的数据信息: " + resultTable);
        int id = resultTable.getEmployeeId(); //查询得到插入的数据id
        CompanyTable updateTable = new CompanyTable(id, "小尾巴", false, 12111223344L, null);
        companyService.saveOrUpdate(updateTable); //根据实体类对象的id修改数据,若id不存在则改为插入数据
        System.out.println("修改后的数据信息: " + companyService.getById(id));
        QueryWrapper<CompanyTable> selectWrapper = Wrappers.query(); //创建查询条件对象
        selectWrapper.in("employee_id", 1, 2, id);
        System.out.println("循环输出id为1,2和刚刚新增的数据: ");
        companyService.list(selectWrapper).forEach(System.out::println); //根据 QueryWrapper 对象的查询条件循环输出查询结果
        System.out.println("删除刚刚新增的数据");
        companyService.removeById(id); //根据主键删除数据
    }
}
