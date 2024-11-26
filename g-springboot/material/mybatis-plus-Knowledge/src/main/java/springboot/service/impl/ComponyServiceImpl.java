package springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import springboot.entity.CompanyTable;
import springboot.mapper.CompanyTableMapper;
import springboot.service.CompanyService;

/**
 * 服务层实现类.
 * <p>除了实现以往的服务层接口,这里继承了 {@code ServiceImpl<Mapper接口名,实体类类名>} 抽象类</p>
 * <p>该抽象类中没有任何抽象方法,但是全部重写了继承接口所继承的接口中的所有方法,因此本类可以不主动实现任何方法</p>
 */
@Service
public class ComponyServiceImpl extends ServiceImpl<CompanyTableMapper, CompanyTable> implements CompanyService {
}
