package springboot.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 初始化组件.用于在服务器启动时执行某些代码.
 * <p>该组件必须实现 {@code CommandLineRunner} 接口并重写 run 方法</p>
 * <p>这里顺便测试了获取文件路径的方法</p>
 */
@Component
public class InitializationComponent implements CommandLineRunner {
    /**
     * 该方法内的代码会在服务器启动时执行一次.
     *
     * @param args 启动服务器时传入主方法的参数
     * @throws Exception 并不清楚会抛出什么异常
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("======正在加载初始化组件======");
        System.out.println("项目文件夹路径: " + System.getProperty("user.dir")); //项目文件夹绝对路径
        // 执行代码时获取该类所在包的绝对路径,在测试环境下执行该方法其打印路径与正常情况不同
        // 其中 Objects.requireNonNull() 表示若括号内对象为 null 则抛出异常
        System.out.println("该类所在包的路径: " + Objects.requireNonNull(this.getClass().getResource("/")).getPath());
        // 获取 resources 文件夹下的 application.yml 文件
        ClassPathResource application = new ClassPathResource("application.yml");
        System.out.println("项目配置文件路径: " + application.getFile().getAbsolutePath()); //打印该文件的绝对路径
    }
}
