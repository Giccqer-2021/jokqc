package mybatis;

import mybatis.mapper.TimeTableMapper;
import mybatis.util.MybatisUtil;

/**
 * 本方法用仅用于获取 time_table 数据表中的所有数据.
 */
public class TimeTableOperation {
    public static void main(String[] args) {
        MybatisUtil.operate(TimeTableMapper.class, mapper -> {
            mapper.selectAll().forEach(System.out::println);
        }, false);
    }
}
