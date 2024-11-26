package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 自定义的登录页面配置类.
 * <p>多数为固定写法,照做便是</p>
 * <p>请尽可能使用函数式语言代替内部类进行书写,否则后果不堪设想</p>
 */
@Configuration
public class LoginConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(fromLogin ->
                fromLogin.loginPage("/loginPage") //自定义跳转到的登陆页面,需要在controller中实现
                        .defaultSuccessUrl("/") //自定义登陆成功跳转页面,默认是"/"或上一次的请求页面
                        .failureUrl("/loginFailed") //自定义登录失败跳转页面
                        .loginProcessingUrl("/login")); //自定义的form表单提交地址
        httpSecurity.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests.
                        //放行的请求,访问这些不会验证账号密码,记得放行静态资源
                        requestMatchers("/loginPage", "/loginFailed", "/script/**").permitAll()
                        .anyRequest().authenticated());//其他的请求一律拦截并进行身份验证
        return httpSecurity.build();
    }
}
