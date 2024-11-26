package springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建springboot工程时所创建的第一个controller.
 */
@RestController
public class HelloController {

    /**
     * springboot 默认首页
     *
     * @return 访问成功信息
     */
    @GetMapping({"/hello", "/"})
    public String helloPage() {
        return "<p>访问springboot服务器成功!</p>";
    }
}
