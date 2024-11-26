package springboot.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.entity.UserEntity;
import springboot.entity.UserExamples;
import springboot.service.UserService;

/**
 * UserService实现类.
 * <p>由于父接口继承了 {@link UserDetailsService} 接口,这里要重写其中的 {@code loadUserByUsername()} 方法</p>
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * 重写的方法,根据用户名得并返回到用户实体以作验证.
     * <p>基本上属于固定写法,若未找到则抛出异常,请勿返回空值</p>
     * <p>该service对象会被SpringSecurity框架使用,并不需要在自定义的controller层的类中引用</p>
     *
     * @param username 传入的用户名
     * @return 返回用户实体, 该实体类需继承 {@link UserDetails}
     * @throws UsernameNotFoundException 在数据库中未找到该用户时抛出该异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserExamples example : UserExamples.values()) { //生产环境中请从数据库中查询
            UserEntity userEntity = example.getUserEntity();
            if (username.equals(userEntity.getUsername())) {
                return userEntity;
            }
        }
        throw new UsernameNotFoundException("用户不存在"); //没查到用户时抛出异常
    }
}
