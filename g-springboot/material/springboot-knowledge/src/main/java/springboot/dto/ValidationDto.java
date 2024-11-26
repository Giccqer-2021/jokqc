package springboot.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 模拟用户组数据传输对象.
 * <p>主要用于测试 bean validation 注解对字段的校验规范</p>
 */
@Data
public class ValidationDto {
    @Positive(message = "id必须大于0") // @Positive 用于数值型数据,该数据必须为正整数
    @NotNull(message = "id不能为空") // @NotNull 对象不可为空
    private Integer id;
    @Size(min = 2, max = 10, message = "name长度必须在2-10之间") // @Size 用于字符串数据,该数据长度必须在min-max之间
    @NotBlank(message = "name不能为空") // @NotBlank 用于字符串数据,数据非空且去除两边空格后不可以是空串
    private String name;
    // @Pattern 用于字符串数据,该数据必须匹配java正则表达式,该表达式声明密码必须是大小写字母或是数字,长度为6-12位
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "密码格式不正确")
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "性别不能为空")
    private Boolean isMale;
    @Max(value = 150, message = "年龄不能超过150") // @Max 用于数值型数据,该数据必须小于等于max
    @Min(value = 0, message = "年龄不能小于0") // @Min 用于数值型数据,该数据必须大于等于min
    @NotNull(message = "年龄不能为空")
    private Integer age;
    @Max(value = 99999999999L, message = "手机号长度必须为11位")
    @Min(value = 10000000000L, message = "手机号长度必须为11位")
    private Long phone;
    @Email(message = "邮箱格式不正确") // @Email 用于字符串数据,该数据必须为邮箱格式
    private String email;
}
