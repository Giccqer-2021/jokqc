package springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.mapper.CompanyTableMapper;

/**
 * 用以测试Mybatis能否从数据库中正确获取数据.
 */
@SpringBootTest
public class MybatisTest {
    @Autowired
    private CompanyTableMapper companyTableMapper;

    @Test
    public void printTest() {
        companyTableMapper.getAllEmployees().forEach(System.out::println);
    }
}
