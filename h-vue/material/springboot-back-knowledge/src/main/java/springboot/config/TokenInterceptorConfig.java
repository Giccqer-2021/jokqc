package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springboot.interceptor.TokenInterceptor;

/**
 * 注册token验证拦截器的配置类.
 */
@Configuration
public class TokenInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] addPathPatterns = {"/token/**"}; //拦截路径
        String[] excludePathPatterns = {"/token/get_token", "/token/get_error_token"}; //放行路径,这两个路径用于获取token
        //TokenInterceptor为自定义类
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
    }
}
