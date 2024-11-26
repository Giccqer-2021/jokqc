package mybatis;

import mybatis.entity.SimpleUserTable;
import mybatis.mapper.SimpleUserTableMapper;
import mybatis.util.MybatisUtil;

import java.util.List;

/**
 * 本方法用于测试 {@link SimpleUserTableMapper} 的查询操作
 */
public class SelectOperation {
    public static void main(String[] args) {
        MybatisUtil.operate(SimpleUserTableMapper.class, mapper -> {
            //查询所有数据
            List<SimpleUserTable> allUsers = mapper.selectAllUsers();
            System.out.println("表内所有数据:");
            allUsers.forEach(System.out::println);
            //查询姓名为 蔡徐坤 的数据
            System.out.println("查询姓名为 蔡徐坤 的数据: " + mapper.selectUserByName("蔡徐坤"));
            //表内数据总条数
            System.out.println("表内数据总条数: " + mapper.userCount());
            //查询条件 性别为男，年龄在20-30之间的数据
            List<SimpleUserTable> usersByConditions = mapper.selectUserByConditions(null, true, (byte) 20, (byte) 30);
            System.out.println("查询条件 性别为男，年龄在20-30之间的数据:");
            usersByConditions.forEach(System.out::println);
        }, false);
    }
}