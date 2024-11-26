package mybatis.mapper;

import java.util.List;
import mybatis.entity.DormitoryTable;
import mybatis.entity.DormitoryTableExample;
import org.apache.ibatis.annotations.Param;

/**
 * mybatis逆向工程自动生成的mapper.
 */
public interface DormitoryTableMapper {
    long countByExample(DormitoryTableExample example);

    int deleteByExample(DormitoryTableExample example);

    int deleteByPrimaryKey(Integer studentId);

    int insert(DormitoryTable row);

    int insertSelective(DormitoryTable row);

    List<DormitoryTable> selectByExample(DormitoryTableExample example);

    DormitoryTable selectByPrimaryKey(Integer studentId);

    int updateByExampleSelective(@Param("row") DormitoryTable row, @Param("example") DormitoryTableExample example);

    int updateByExample(@Param("row") DormitoryTable row, @Param("example") DormitoryTableExample example);

    int updateByPrimaryKeySelective(DormitoryTable row);

    int updateByPrimaryKey(DormitoryTable row);
}