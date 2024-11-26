package mybatis;

import mybatis.entity.SimpleUserTable;
import mybatis.mapper.SimpleUserTableMapper;
import mybatis.util.MybatisUtil;

/**
 * 本方法用于测试 {@link SimpleUserTableMapper} 的增删改操作.
 */
public class DataAdaptOperation {
    public static void main(String[] args) {
        MybatisUtil.operate(SimpleUserTableMapper.class, mapper -> {
            //插入数据
            mapper.insertIntoUserTable(new SimpleUserTable(null, "王大锤", true, (byte) 26, 23333333333L, "南京市浦西新区", null));
            System.out.println("执行插入数据操作,其结果为: " + mapper.selectUserByName("王大锤"));
            //修改数据
            mapper.updateUserTable(new SimpleUserTable(null, "王晓晓", false, (byte) 28, 23333333333L, "南京市浦西新区", "巴啦啦能量.米卡哇", "王大锤"));
            System.out.println("执行更新数据操作,其结果为: " + mapper.selectUserByName("王晓晓"));
            //删除数据
            mapper.deleteUserByName("王晓晓");
            System.out.println("已执行删除数据操作");
        }, false);
    }
}
