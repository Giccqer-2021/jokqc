package springboot.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springboot.annotation.CustomAnnotation;

/**
 * aop 配置类,使用aop切面编程插入"钩子"来监视或控制某些方法的运行过程.
 * <p>该编程方法一般用于调用日志的输出,或是使得自定义注解产生某些效果</p>
 */
@Aspect
@Configuration
public class AOPMonitorConfig {
    /**
     * 切点表达式,很复杂,详见教程.
     * <p>表示监视 {@code controller} 包下 {@code DefaultController} 类中所有 public 方法</p>
     */
    private static final String POINTCUT_EXPRESSION = "execution(public * springboot.controller.DefaultController.*(..))";

    /**
     * 使用aop切片编程,执行 POINTCUT_EXPRESSION 所描述的方法时会监视该方法并执行某些方法.
     * <p>使用 {@code @Around("方法描述")} 注解环绕监视该方法的执行过程,可以获取到目标方法的入参和返回值</p>
     * <p>使用 {@code @Order(整数)} 注解,若存在多个同类型aop方法,该注解决定了这些方法的执行顺序,值越小优先级越高</p>
     *
     * @param joinPoint 表示目标方法
     * @return 控制被监视的方法返回的结果
     * @throws Throwable 目标方法抛出的异常
     */
    @Around(POINTCUT_EXPRESSION)
    @Order(1)
    public Object aroundByExpression(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("======AOP监视环绕前(使用 express描述 )======");
        Object result = joinPoint.proceed(); // 允许被监视的方法执行, result 为被监视的方法的返回值
        System.out.println("======AOP监视环绕后(使用 express描述 )======");
        return result; //要写上,声明该方法能正常返回结果
    }

    /**
     * 使aop切片编程监视具有某自定义注解方法的相关配置.
     * <p>@{@link CustomAnnotation} 为自定义注解,为了使该注解能"能起作用",使用aop监控使用该注解的方法</p>
     * <p>本方法可以不写后面的自定义注解类形参,如若此做, {@code Around("")} 中的注解名要写全该注解的完整路径</p>
     * <p>监控注解的aop方法会"包裹"描述类express的aop方法去执行</p>
     *
     * @param joinPoint        表示目标方法
     * @param customAnnotation 自定义注解,用作形参时可获取其中相关的属性
     * @return 控制被监视的方法返回的结果
     * @throws Throwable 目标方法抛出的异常
     */
    @Around("@annotation(customAnnotation)") // @annotation() 括号中的字符串要与本方法CustomAnnotation注解形参的名字一致
    public Object aroundByAnnotation(ProceedingJoinPoint joinPoint, CustomAnnotation customAnnotation) throws Throwable {
        System.out.println("======AOP监视环绕前(使用 annotation注解 )======");
        System.out.println("自定义注解的value值: " + customAnnotation.value()); //获取自定义注解的value值
        Object result = joinPoint.proceed();
        System.out.println("======AOP监视环绕后(使用 annotation注解 )======");
        return result;
    }
}
