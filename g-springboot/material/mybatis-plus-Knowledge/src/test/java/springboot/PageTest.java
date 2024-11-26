package springboot;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.entity.CompanyTable;
import springboot.mapper.CompanyTableMapper;

import java.util.List;

/**
 * 本测试用于分页查询.
 */
@SpringBootTest
public class PageTest {
    @Autowired
    private CompanyTableMapper companyTableMapper;

    @Test
    public void getPageTest() {
        Page<CompanyTable> page = new Page<>(1, 2);// 当前页码，每页显示条数
        companyTableMapper.selectPage(page, null);
        List<CompanyTable> records = page.getRecords();
        System.out.println("循环打印第一页结果: ");
        records.forEach(System.out::println);
        System.out.println("当前页页码：" + page.getCurrent());
        System.out.println("每页显示条数：" + page.getSize());
        System.out.println("总数据条数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
        System.out.println("是否有上一页：" + page.hasPrevious());
        System.out.println("是否有下一页：" + page.hasNext());
    }
}
