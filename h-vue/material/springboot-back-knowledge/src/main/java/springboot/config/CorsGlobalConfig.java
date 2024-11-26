package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置.
 * <p>推荐使用这种方法</p>
 */
@Configuration
public class CorsGlobalConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")              //允许访问的路径
                //允许的域名,如果需要声明特定的端口号则加上字样如:80,考虑到浏览器的问题不建议特意声明80号端口
                .allowedOrigins("http://localhost")
                .allowCredentials(true)                     //允许携带认证信息cookie
                .maxAge(3600)                               //重新预检验跨域的缓存时间,单位为秒
                .allowedHeaders("*")                        //允许的请求头
                .allowedMethods("GET", "POST", "PUT", "DELETE");//允许的请求方法
    }
}
