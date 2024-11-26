package springboot.dto;

import lombok.Data;
import springboot.annotation.AdminPasswordAnnotation;

/**
 * 本类用于实践自定义的 @AdminPasswordAnnotation 注解.
 */
@Data
public class AdminPasswordDto {
    /**
     * 这里假设管理员名字(注解value值)是以实玛利,密码来自前端发来的数据.
     */
    @AdminPasswordAnnotation("以实玛利")
    private String password;
}
