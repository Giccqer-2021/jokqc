package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * 启动类.
 * <p>这里,添加 @{@link EnableMethodSecurity} 注解来开启用注解声明权限的方法,也可以写在配置类上</p>
 */
@SpringBootApplication
@EnableMethodSecurity
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}
