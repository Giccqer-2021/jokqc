package springboot.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import springboot.annotation.AdminPasswordAnnotation;

/**
 * 自定义校验规则以使 @{@link AdminPasswordAnnotation} 注解校验有效.
 * <p>需实现 {@link ConstraintValidator}<自定义的校验注解,要校验的字段类型> 泛型接口,至少重写 isValid() 方法</p>
 */
public class AdminPasswordValidator implements ConstraintValidator<AdminPasswordAnnotation, String> {
    /**
     * 自定义注解的value值,即管理员名字,在注解初始化时赋值.
     */
    String admin;

    /**
     * 注解初始化时调用,将注解的value值赋值给admin全局字段.
     * <p>该方法有其默认的实现方法,可以不重写</p>
     *
     * @param constraintAnnotation 自定义的注解实例
     */
    @Override
    public void initialize(AdminPasswordAnnotation constraintAnnotation) {
        this.admin = constraintAnnotation.value();
    }

    /**
     * 自定义具体校验规则.
     * <p>这里假设猪猪侠,灰太狼,以实玛利是管理员,只有输入口令正确才会校验通过</p>
     *
     * @param value   校验字段的值
     * @param context 一般用不上
     * @return 为 true 时校验通过, false 时校验失败
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (this.admin.equals("猪猪侠") && value.equals("超级棒棒糖")) return true;
        else if (this.admin.equals("灰太狼") && value.equals("我还会回来的")) return true;
        else return this.admin.equals("以实玛利") && value.equals("都是你的错");
    }
}
