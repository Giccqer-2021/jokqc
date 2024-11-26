package springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.component.CustomComponent;

/**
 * 用以测试自定义组件的测试类.
 */
@SpringBootTest
public class CustomComponentInfoTest {
    @Autowired
    private CustomComponent customComponent;

    @Test
    public void printTest() {
        System.out.println("自定义网站: " + customComponent.getName());
        System.out.println("自定义网址: " + customComponent.getWebsite());
        System.out.println("自定义网站和网址: " + customComponent.getNameAndWebsite());
    }
}
