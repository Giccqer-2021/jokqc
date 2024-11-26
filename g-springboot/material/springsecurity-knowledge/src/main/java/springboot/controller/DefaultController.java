package springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {

    @GetMapping("/hello")
    public @ResponseBody String helloPage() {
        return "<p>你好,中国</p>";
    }
}
