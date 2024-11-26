package springboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 自定义登录页面的控制方法,每个方法映射一个 thymeleaf 网页.
 * <p>需要事先写好相应的 thymeleaf 模板</p>
 */
@Controller
public class LoginController {
    @GetMapping(value = "/loginPage") //登录页面
    public String toLogin() {
        return "loginPage";
    }

    @GetMapping(value = "/") //登录成功页面
    public String loginSuccess(Model model) {
        //获取身份验证对象,固定写法照写便是
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName(); //用户名
        model.addAttribute("user", name); //写入 attribute , 在模板中通过 ${user} 展示
        return "loginSuccess";
    }

    @GetMapping(value = "/loginFailed") //登录失败页面
    public String loginFail() {
        return "loginFailed";
    }
}
