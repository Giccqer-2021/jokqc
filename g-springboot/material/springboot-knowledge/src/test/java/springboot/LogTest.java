package springboot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 判断测试方法及日志能否正常运转的测试类.
 * <p>主类中的主方法必须能正常运行,该类上添加 {@code @SpringBootTest} 标记为 springboot 测试类</p>
 * <p>如果仅有一个主类则@其中的 value值 可省略</p>
 * <p>关于 {@code @Slf4j}:注解，可以代替诸如</P>
 *  <blockquote><pre>
 *      {@code private final Logger log = LoggerFactory.getLogger(LoggerController.class);}
 *  </pre></blockquote>
 *  <P>这样的语句，且即使没有创建 log 实例，也可以直接使用它。</P>
 *  <p>使用该注解则必须导入 lombok 依赖</p>
 */
@SpringBootTest(classes = springboot.Starter.class)
@Slf4j
public class LogTest {
    @Test
    public void printTest() {
        log.trace("日志输出 trace");
        log.debug("日志输出 debug");
        log.info("日志输出 info");
        log.warn("日志输出 warn");
        log.error("日志输出 error");
    }
}
