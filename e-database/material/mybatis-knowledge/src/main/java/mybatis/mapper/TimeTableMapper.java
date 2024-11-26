package mybatis.mapper;

import mybatis.entity.TimeTable;

import java.util.List;

/**
 * 用来对 time_table 数据表中的数据进行操作的mapper.
 */
public interface TimeTableMapper {
    List<TimeTable> selectAll();
}
