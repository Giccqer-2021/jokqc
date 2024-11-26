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

/**
 * 封装数据对象的实体类.
 * <p>一般情况下前后端数据传输要更复杂得多,这里用来作模拟因此只添加id,name等字段</p>
 * <p>由于该实体类用作登陆验证,所以要继承 {@link UserDetails} 接口并实现其中的三个必写方法</p>
 * <p>出于保护密码不被泄露的原则,请勿将该类直接用作jwt载荷的数据体类,或可以在敏感字段上添加 {@code @JsonIgnore} 注解防止该字段被写入载荷</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    /**
     * 用户id.
     * <p>不要定义为基本类型(如int)</p>
     */
    private Integer id;
    /**
     * 用户名.
     * <p>为了避免使用getter方法时混淆,不建议将用户名命名为 username ,或是将密码名命名为 password</p>
     */
    private String name;
    /**
     * 加密后的密码,需提前配置加密方式使程序可辨别.
     * <p>{@code @JsonIgnore} 表示在json序列化类时忽略该属性</p>
     */
    @JsonIgnore
    private String userPassword;
    /**
     * 角色与权限列表.
     * <p>通常情况下需要使用数据库进行联表查询,并使用 List&lt;权限封装类&gt; 封装数据</p>
     * <p>为方便起见,这里直接使用字符串</p>
     */
    private List<String> PermissionList;

    /**
     * 重写的方法,返回的对象声明了以何种方法来定义用户权限.
     *
     * @return 权限集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();//定义权限集合
        //固定写法,最后将相关的权限字符串传入即可
        PermissionList.forEach(permission -> grantedAuthorityArrayList.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorityArrayList;
    }

    /**
     * 重写的方法,声明该用户的密码.
     *
     * @return 会被调用的密码
     */
    @Override
    public String getPassword() {
        return this.userPassword;
    }

    /**
     * 重写的方法,声明该用户的用户名.
     *
     * @return 会被调用的用户名
     */
    @Override
    public String getUsername() {
        return this.name;
    }
}
