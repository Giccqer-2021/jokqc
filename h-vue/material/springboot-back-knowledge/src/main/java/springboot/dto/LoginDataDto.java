package springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 该类用于封装从前端接收到的post请求体中的json数据.
 * <p>该类的属性名称要与前端定义的json属性名称相同</p>
 */
@Data
public class LoginDataDto {
    /**
     * 如果前端定义的json属性名与属性名称不相同,可以使用@JsonProperty注解来指定属性名.
     */
    @JsonProperty("userAccount")
    private String userAccount;
    private String userPassword;
    private Boolean rememberMe;
}
