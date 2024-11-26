package springboot.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义的组件.
 * <p>使用 {@code @Component} 声明,使用 {@code @ConfigurationProperties()}注解标记要注入的属性值,其中的 value 或 prefix 值必须用
 * 小写横线相连 的写法声明,不可以使用大小驼峰的写法否则报错,不过在配置文件中使用小驼峰写法是允许的.</p>
 * <p>在 application.yml 文件配置如下:
 * <blockquote><pre>
 * customInfo:
 *   name: 百度
 *   website: https://www.baidu.com/
 * </pre></blockquote></p>
 * <p>可以在测试类中进行打印测试,相关测试类为:{@code CustomComponentInfoTest}</p>
 * <p>使用自定义的属性建议导入 {@code spring-boot-configuration-processor} 依赖以在构建后生成相应的配置说明文件</p>
 */
@Component
@ConfigurationProperties(prefix = "custom-info")
@Data
public class CustomComponent {
    /**
     * 百度.
     */
    private String name;
    /**
     * https:// www.baidu.com/.
     */
    private String website;
    /**
     * 网站:百度---网址:https://www.baidu.com/
     */
    private String nameAndWebsite;
}
