package springboot.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 用于测试SpringBoot的拦截器interceptor相关的视图网址配置.
 */
@Controller
@RequestMapping("/interceptor")
public class InterceptorController {

    /**
     * 该网址在配置文件中已声明需要被拦截.
     *
     * @return 返回的页面内容
     */
    @GetMapping("/blocked-page")
    public @ResponseBody String blockedPage() {
        return "<p>该页面的访问请求被拦截但验证通过</p>";
    }

    /**
     * 该网址在配置文件中已声明不会被拦截.
     *
     * @return 返回的页面内容
     */
    @GetMapping("/allowed-page")
    public @ResponseBody String allowedPage() {
        return "<p>该页面的访问请求未被拦截</p>";
    }

    /**
     * 该网址会被拦截并验证其token,但验证会被通过.
     * <p>验证通过后,便可以访问 /blocked-page 网址</p>
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param session  会话对象
     * @throws ServletException servlet异常
     * @throws IOException      io异常
     */
    @GetMapping("/allowed-token-page")
    public void allowedTokenPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        session.setAttribute("token", "123456");//模拟设置token的过程
        request.getRequestDispatcher("/interceptor/blocked-page").forward(request, response);//现在允许访问了
    }
}
