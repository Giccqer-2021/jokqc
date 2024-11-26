package springboot.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import springboot.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自行封装的用来传递用户相关信息的信息类.
 * <p>为保密起见,相对于实体类这里移除了对密码的封装,实际情况可能会更复杂些</p>
 * <p>记得加入一个无参构造</p>
 */
@Data
@NoArgsConstructor
public class UserInfo {
    private Integer id;
    private String name;
    private List<String> PermissionList;

    public UserInfo(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.name = userEntity.getName();
        this.PermissionList = userEntity.getPermissionList();
    }

    /**
     * 该方法在配置文件中被调用.
     * <p>由于不使用实体类本身,所以要重写该方法来满足参数传递的需求</p>
     * <p>可以考虑将其封装入util工具类使用静态方法调用</p>
     *
     * @return
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();
        PermissionList.forEach(permission -> grantedAuthorityArrayList.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorityArrayList;
    }
}
