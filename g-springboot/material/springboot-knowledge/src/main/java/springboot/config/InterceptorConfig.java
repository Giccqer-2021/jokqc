package springboot.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.interceptor.LoginInterceptor;

/**
 * 对SpringBoot拦截器进行配置的配置类.
 * <p>需继承 {@code WebMvcConfigurer} 接口重写 {@code addInterceptors()} 方法,声明哪些网页需要被拦截,哪些不需要</p>
 * <p>需事先创建 {@code HandlerInterceptor} 接口实例来自定义拦截规则</p>
 * <p>为防止警告,形参中添加 {@code @NotNull} 注解</p>
 *
 * @see LoginInterceptor
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        String[] addPathPatterns = {"/interceptor/**"};//要拦截的网页白名单
        //白名单中排除在外的网页
        String[] excludePathPatterns = {"/interceptor/allowed-page", "/interceptor/allowed-token-page"};
        //LoginInterceptor类为自定义的继承HandlerInterceptor接口的实例
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}
