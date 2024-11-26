package springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import springboot.entity.CompanyTable;

import java.util.List;


/**
 * MyBatis-Plus 的 Mapper 接口.
 * <p>必须声明继承 {@code BaseMapper<实体类类名>} 方可在编程时使用 Mybatis-Plus 中的方法</p>
 * <p>同时,也可以自己声明一些方法,并创建对应的 xml 文件映射这些方法,这与普通 MyBatis 的使用方法相同</p>
 */
@Mapper
@Repository
public interface CompanyTableMapper extends BaseMapper<CompanyTable> {
    List<CompanyTable> selectAll();
}
