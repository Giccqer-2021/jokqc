package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SpringBoot框架的启动类.
 * <p>直接使用main方法启动，将该类声明为启动类则注解 @{@link SpringBootApplication} 是必不可少的，而其他注解的含义是：</p>
 * <p>@{@link MapperScan} -&gt;启动包扫描，声明被扫描的包中的文件是持久层的mapper文件并导入，其value值就是要扫描的包的位置。
 * 如果不使用该注解则需要使用注解{@code  @Mapper}和{@code @Repository}在mapper类上标记声明 </p>
 * <p>@{@link EnableTransactionManagement} -&gt;启用事物管理，如果有必要的话。如果想要启用数据操作回滚功能，则必须要使用此注解。
 * 同时，在service层需要在方法上添加 {@code @Transactional} 注解才能启用事物回滚。</p>
 * <p>当项目需要部署 war 包时,需继承 {@link SpringBootServletInitializer} 类并重写其中的 {@code configure}方法</p>
 * <p>注解 @{@link ServletComponentScan} 表示扫描传统的 servlet 类型的包下的配置类</p>
 */
@SpringBootApplication
//@MapperScan("springboot.dao")
@EnableTransactionManagement
//@ServletComponentScan("springboot.filter")
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

//    部署war包时,需要继承 SpringBootServletInitializer 类并重写该方法,行业规范,照做便是
    /*@Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Starter.class);
    }*/
}
