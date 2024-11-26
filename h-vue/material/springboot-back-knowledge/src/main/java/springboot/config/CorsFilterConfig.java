package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域过滤器.
 * <p>不推荐使用这种方法,它会覆盖其他跨域配置设置</p>
 */
//@Configuration
public class CorsFilterConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);                       //允许携带认证信息cookie
        config.addAllowedOrigin("http://localhost");//允许的域名,如果需要声明特定的端口号则加上如 :80 字样
        config.addAllowedHeader("*");                           //允许的请求头
        config.addAllowedMethod("*");                           //允许的请求方法
        source.registerCorsConfiguration("/**", config); //允许访问的路径
        return new CorsFilter(source);
    }
}
