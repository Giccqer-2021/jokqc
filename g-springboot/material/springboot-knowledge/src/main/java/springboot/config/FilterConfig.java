package springboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.filter.CustomFilter;

/**
 * 配置 servlet 过滤器.
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CustomFilter> filterBean() {
        FilterRegistrationBean<CustomFilter> filter = new FilterRegistrationBean<>(new CustomFilter());
        //表示过滤全部网页
        filter.addUrlPatterns("/*");
        return filter;
    }
}
