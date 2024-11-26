package springboot.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.entity.UserEntity;
import springboot.entity.UserExamples;
import springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserExamples example : UserExamples.values()) {
            UserEntity userEntity = example.getUserEntity();
            if (username.equals(userEntity.getUsername())) {
                return userEntity;
            }
        }
        throw new UsernameNotFoundException("用户不存在");
    }
}
