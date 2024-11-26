package springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.message.Notification;

/**
 * 登陆"页面"相关控制.
 * <p>由于后端不再提供页面渲染,所以只会返回相关的json消息数据</p>
 */
@RestController
public class LoginController {
    @GetMapping(value = "/loginPage")
    public Notification loginPage() {
        return new Notification("不支持访问该页面或执行该请求!");
    }

    @GetMapping(value = "/")
    public Notification loginSuccess(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        model.addAttribute("user", name);
        return new Notification("页面登录成功!");
    }

    @GetMapping(value = "/loginFailed")
    public Notification loginFail() {
        return new Notification("账号或密码错误!");
    }
}
