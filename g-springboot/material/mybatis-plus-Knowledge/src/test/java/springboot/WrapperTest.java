package springboot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.entity.CompanyTable;
import springboot.mapper.CompanyTableMapper;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class WrapperTest {

    @Autowired
    private
    CompanyTableMapper companyTableMapper;

    /**
     * 测试 QueryWrapper ,第一个方法.
     */
    @Test
    public void wrapperUseTest() {
        QueryWrapper<CompanyTable> wrapper = Wrappers.query(); //创建查询条件,等同于 new QueryWrapper<>();
        List<CompanyTable> companyTables = companyTableMapper.selectList(wrapper);
        System.out.println("根据 QueryWrapper 对象声明的查询条件输出所有数据: ");
        companyTables.forEach(System.out::println); //由于没有定义任何条件,输出所有数据
    }

    /**
     * 根据 QueryWrapper 构造查询条件.
     */
    @Test
    public void wrapperSelectTest() {
        QueryWrapper<CompanyTable> wrapper = new QueryWrapper<>();
        wrapper.select("employee_id", "employee_phone", "employee_name"); //只查询指定字段
        wrapper.like("notes", "%偷懒%") //模糊查询
                .gt("employee_id", 1) //小于
                .lt("employee_id", 4) //大于
                .eq("is_male", true) //等于
                .or() //或,除了这里其他语句之间默认加 and 连接
                .eq("employee_phone", "12323234542"); //等于
        List<CompanyTable> companyTables = companyTableMapper.selectList(wrapper);
        System.out.println("根据 QueryWrapper 对象声明的查询条件输出所有数据: ");
        companyTables.forEach(System.out::println);
    }

    /**
     * 使用 UpdateWrapper 更新数据.
     */
    @Test
    public void updateWrapperTest() {
        //插入一条数据,然后查询其获得的id
        CompanyTable insertTable = new CompanyTable(null, "橡胶君", true, 99988811122L, "大香蕉一条大香蕉");
        companyTableMapper.insert(insertTable);
        HashMap<String, Object> selectCondition = new HashMap<>();
        selectCondition.put("employee_name", "橡胶君");
        CompanyTable resultTable = companyTableMapper.selectByMap(selectCondition).getFirst();
        System.out.println("已插入数据: " + resultTable);
        int id = resultTable.getEmployeeId();
        //这里使用 Wrappers.update() 创建 UpdateWrapper 对象用来更新指定条件对的数据
        UpdateWrapper<CompanyTable> wrapper = Wrappers.update();
        wrapper.eq("employee_name", "橡胶君"); //指定要更新的数据的条件
        wrapper.set("employee_name", "橡胶君2"); //要更新的数据
        wrapper.set("employee_phone", 99988811122L); //要更新的数据
        wrapper.set("notes", null); //要更新的数据
        //更新数据,由于前者为null所以要删除数据的条件和更新数据的方法都写在了 wrapper 中
        companyTableMapper.update(null, wrapper);
        System.out.println("第一次更新后的数据: " + companyTableMapper.selectById(id));
        UpdateWrapper<CompanyTable> wrapper2 = Wrappers.update();
        CompanyTable checkTable = new CompanyTable(null, "橡胶君3", false, 12312341234L, "我在东北玩泥巴");
        wrapper2.eq("employee_name", "橡胶君2");
        // 以后者(wrapper)为更新数据的条件,前者实体类对象为更新数据的内容,执行更新数据操作
        companyTableMapper.update(checkTable, wrapper2);
        System.out.println("第二次更新后的数据: " + companyTableMapper.selectById(id));
        companyTableMapper.deleteById(id); //删除数据
    }
}
