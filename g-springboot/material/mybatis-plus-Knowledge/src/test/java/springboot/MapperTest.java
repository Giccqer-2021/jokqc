package springboot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.entity.CompanyTable;
import springboot.mapper.CompanyTableMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 测试 MyBatis-Plus 中 mapper 层的使用.
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private CompanyTableMapper companyTableMapper;

    /**
     * 测试数据库连接,打印表中所有数据,
     */
    @Test
    public void connectionTest() {
        // selectList() 中为null时返回所有数据
        List<CompanyTable> companyList = companyTableMapper.selectList(null);
        System.out.println("mapper循环打印数据表信息: ");
        companyList.forEach(System.out::println);
    }

    /**
     * 测试 mapper 查询功能.
     */
    @Test
    public void selectTest() {
        CompanyTable companyID1 = companyTableMapper.selectById(1); //根据主键id查询(id类型不一定是数字)
        System.out.println("id为1的数据: " + companyID1);
        //根据id所组成的集合查询
        List<CompanyTable> companiesID123 = companyTableMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println("id为1,2,3的数据: ");
        companiesID123.forEach(System.out::println);
        HashMap<String, Object> selectCondition = new HashMap<>();//使用hashmap设定查询条件
        selectCondition.put("employee_name", "王晓晓");
        List<CompanyTable> companyList = companyTableMapper.selectByMap(selectCondition); //根据该查询条件查询
        System.out.println("姓名为王晓晓的数据: " + companyList.getLast());
    }

    /**
     * 测试 mapper 插入数据功能.
     */
    @Test
    public void insetTest() {
        CompanyTable companyTable = new CompanyTable(null, "马牛逼", true, 1234567890L, "这人敢吃冰淇淋");
        int result = companyTableMapper.insert(companyTable);
        System.out.println("插入数据条数: " + result);
    }

    /**
     * 测试 mapper 更新数据功能,使用刚刚插入的数据.
     */
    @Test
    public void updateTest() {
        HashMap<String, Object> selectCondition = new HashMap<>(); //使用HashMap输入查询条件
        selectCondition.put("employee_name", "马牛逼");// 由于主键为自增主键,所以并不清楚该数据在表中的id,需要通过查询获得
        int id = companyTableMapper.selectByMap(selectCondition).getLast().getEmployeeId();
        CompanyTable companyTable = new CompanyTable(id, "马牛逼", false, 9000000000L, null);
        companyTableMapper.updateById(companyTable);
        System.out.println("数据修改后的值: " + companyTableMapper.selectById(id));
    }

    /**
     * 测试 mapper 删除数据功能,根据查询条件删除.
     */
    @Test
    public void deleteTest() {
        HashMap<String, Object> selectCondition = new HashMap<>(); //使用HashMap输入查询条件
        selectCondition.put("employee_name", "马牛逼");
        int deleteCount = companyTableMapper.deleteByMap(selectCondition);
        System.out.println("已删除的信息条数: " + deleteCount);
    }

    /**
     * 测试自定义查询语句,打印所有数据.
     */
    @Test
    public void customSelectTest() {
        System.out.println("自定义select语句查询并循环打印结果:");
        companyTableMapper.selectAll().forEach(System.out::println);
    }
}
