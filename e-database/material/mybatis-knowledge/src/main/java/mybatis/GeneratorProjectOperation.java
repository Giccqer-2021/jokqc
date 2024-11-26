package mybatis;

import mybatis.entity.DormitoryTable;
import mybatis.entity.DormitoryTableExample;
import mybatis.mapper.DormitoryTableMapper;
import mybatis.util.MybatisUtil;

/**
 * 本方法用于测试mybatis逆向工程生成的 {@link DormitoryTableMapper} 的增删改查操作.
 */
public class GeneratorProjectOperation {
    public static void main(String[] args) {
        MybatisUtil.operate(DormitoryTableMapper.class, mapper -> {
            // 根据主键查询某条数据
            System.out.println("id为1的宿舍学生信息: " + mapper.selectByPrimaryKey(1));
            // 自定义 example 类,配置查询条件,根据该条件进行查询
            DormitoryTableExample selectExample = new DormitoryTableExample();
            selectExample.setDistinct(true); // 查询结果去除重复数据
            DormitoryTableExample.Criteria selectCriteria = selectExample.createCriteria();
            selectCriteria.andIsMaleBetween((byte) 1, (byte) 1).andNotesLike("%李狗蛋%"); // 查询男性,其笔记中包含"李狗蛋"三个字
            System.out.println("使用example类查询结果: " + mapper.selectByExample(selectExample)); // 返回查询结果
            // 插入数据,然后查询插入的数据
            mapper.insert(new DormitoryTable(null, "王小锤", (byte) 1, 88888888880L, "八十,八十,八十!"));
            DormitoryTableExample insertExample = new DormitoryTableExample();
            insertExample.createCriteria().andStudentNameEqualTo("王小锤");
            System.out.println("新插入的数据: " + mapper.selectByExample(insertExample));
            // 修改刚刚插入的数据,然后查询. updateByExampleSelective 表示只修改非空的字段, updateByExample 表示修改所有字段(会赋予空值)
            mapper.updateByExampleSelective(new DormitoryTable(null, "李大铲", null, 66666666660L, null), insertExample);
            DormitoryTableExample upDateExample = new DormitoryTableExample();
            upDateExample.createCriteria().andStudentNameEqualTo("李大铲");
            System.out.println("更新后的数据: " + mapper.selectByExample(upDateExample));
            // 删除刚刚修改的数据
            mapper.deleteByExample(upDateExample);
            System.out.println("已删除数据");
        }, false);
    }
}
