package springboot.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解,使用aop切面编程来让这个注解起到实际作用.
 */
@Target(ElementType.METHOD) // 注解作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 注解保留在运行时
@Documented // 所有使用该注解的地方,在生成javadoc文档时保留该注解的描述
public @interface CustomAnnotation {
    String value() default "";
}
