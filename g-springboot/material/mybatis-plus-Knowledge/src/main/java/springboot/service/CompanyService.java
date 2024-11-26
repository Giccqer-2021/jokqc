package springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import springboot.entity.CompanyTable;

/**
 * 服务层接口.
 * <p>需要继承 {@code IService<实体类类名>} 接口方可在编程时使用 Mybatis-Plus 中的方法</p>
 */
public interface CompanyService extends IService<CompanyTable> {
}
