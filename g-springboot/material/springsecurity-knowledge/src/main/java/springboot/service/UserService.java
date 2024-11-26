package springboot.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 该服务层接口专门用于SpringSecurity的用户账号密码验证.
 * <p>必须声明继承 {@link UserDetailsService} 接口</p>
 */
public interface UserService extends UserDetailsService {
}
