package springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    private Integer id;
    private String name;
    @JsonIgnore
    private String userPassword;
    private List<String> PermissionList;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", PermissionList=" + PermissionList +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();
        PermissionList.forEach(permission -> grantedAuthorityArrayList.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorityArrayList;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.name;
    }
}
