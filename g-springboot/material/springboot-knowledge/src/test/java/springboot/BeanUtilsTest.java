package springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.entity.CompanyTable;

/**
 * 仅用于测试 BeanUtils 工具类复制对象的方法
 */
@SpringBootTest
public class BeanUtilsTest {
    /**
     * 测试复制对象.
     * <p>原对象和被复制的对象中的属性名称和类型必须要相同，否则会抛出异常</p>
     */
    @Test
    public void copyObjectTest() {
        CompanyTable originalEmployee = new CompanyTable(); //创建初始对象并为其赋值的过程
        originalEmployee.setEmployeeId(1);
        originalEmployee.setEmployeeName("周杰伦");
        originalEmployee.setIsMale(true);
        originalEmployee.setEmployeePhone(12345678901L);
        originalEmployee.setNotes("大家好,我是周杰伦,一颗最闪耀的星");
        CompanyTable copyEmployee = new CompanyTable(); //创建一个所有属性皆为空值的对象,用来存放复制后的对象
        BeanUtils.copyProperties(originalEmployee, copyEmployee); //复制对象
        copyEmployee.setEmployeeName("堇紫泪滴"); //将复制后的对象的姓名修改为"堇紫泪滴"
        // 二者的姓名属性并不相同,证明二者并非同一对象
        System.out.println("初始员工姓名: " + originalEmployee.getEmployeeName());
        System.out.println("被修改后的员工姓名: " + copyEmployee.getEmployeeName());
    }
}
