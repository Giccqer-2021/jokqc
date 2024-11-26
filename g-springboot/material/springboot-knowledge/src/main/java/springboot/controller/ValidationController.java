package springboot.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.dto.AdminPasswordDto;
import springboot.dto.ValidationDto;

/**
 * 本页面演示了使用 bean validation 注解对前端提交的数据进行校验.
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {

    /**
     * 模拟用户注册页面,使用get方法.
     * <p>{@link ValidationDto} 类中所有字段皆有严格的数据校验规则,如果想要让数据校验生效则需要使用 @{@link Validated} 注解</p>
     * <p>除此之外没必要添加更多的注解,不过或许使用 {@code @RequestParam} 更合适?</p>
     *
     * @param validationDto 用户输入的数据,封装在dto对象中
     * @return 你输入什么数据就返回什么数据(以json形式)
     */
    @GetMapping(value = "/register")
    public ValidationDto getValidationPage(@Validated ValidationDto validationDto) {
        System.out.println("Get方法收到来自前端的数据: " + validationDto);
        return validationDto;
    }

    /**
     * 同上,不过使用的post方法.
     * <p>前端使用请求体返回数据,不过 @{@link RequestBody} 注解是可选的</p>
     * <p>如果加入该注解,前端请求体返回的数据必须是 json 格式</p>
     *
     * @param validationDto 用户输入的数据,封装在dto对象中
     * @return 你输入什么数据就返回什么数据(以json形式)
     */
    @PostMapping(value = "/register")
    public ValidationDto postValidationPage(@RequestBody @Validated ValidationDto validationDto) {
        System.out.println("Post方法收到来自前端的数据: " + validationDto);
        return validationDto;
    }

    /**
     * 模拟管理员授权页面.
     * <p>本方法主要用于测试自定义的校验注解和规则是否生效</p>
     *
     * @param passwordDto 管理员输入的密码,使用专用的校验类封装在dto对象中
     * @return 授权成功后要返回的信息
     */
    @GetMapping(value = "/admin")
    public String adminPage(@Validated AdminPasswordDto passwordDto) {
        System.out.println("前端发来的管理员密码: " + passwordDto);
        return "<p>管理员身份认证成功!</p>";
    }
}
