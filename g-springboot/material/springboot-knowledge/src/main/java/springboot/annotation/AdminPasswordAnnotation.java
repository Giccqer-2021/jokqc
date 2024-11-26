package springboot.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import springboot.validator.AdminPasswordValidator;

import java.lang.annotation.*;

/**
 * 自定义的用来校验管理员密码的注解.
 * <p>会被写死在java代码中,一般不会这么写,这里仅用作演示</p>
 * <p>在创建注解时,你需要添加 validator 依赖提供的专有注解 {@code @Constraint(自定义的校验方法实现类.class)} </p>
 * <p>并添加一些固定写法的抽象方法使该类生效</p>
 */
@Target(ElementType.FIELD) // 注解作用在字段上
@Retention(RetentionPolicy.RUNTIME) // 注解保留在运行时
@Documented  // 所有使用该注解的地方,在生成javadoc文档时保留该注解的描述
@Constraint(validatedBy = AdminPasswordValidator.class) // 该注解需要一个自定义的实现类来完成校验
public @interface AdminPasswordAnnotation {
    /**
     * 本次负责授权的管理员的名字.
     * <p>不用怀疑,这玩意就是个抽象方法而不是字段</p>
     * <p>你甚至可以通过实现本注解接口来重写这个方法</p>
     *
     * @return 本次负责授权的管理员的名字
     */
    String value() default "";

    /**
     * message() 方法为必须添加的,名称固定的注解方法,该方法的用法也是固定的.
     *
     * @return 发生错误时, 返回的默认错误信息提示
     */
    String message() default "管理权限密码输入错误";

    Class<?>[] groups() default {}; //该方法并未被使用,但根据规范,需要添加

    Class<? extends Payload>[] payload() default {}; //该方法并未被使用,但根据规范,需要添加
}
