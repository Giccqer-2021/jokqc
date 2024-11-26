package springboot.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import springboot.config.InterceptorConfig;

/**
 * SpringBoot的interceptor拦截器.
 * <p>用于账户密码或是token令牌验证,需在配置类中配置并生效</p>
 * <p>为了防止idea弹出警告,这个拦截器添加了包注解 {@code @NonNullApi}</p>
 *
 * @see InterceptorConfig
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("正在验证...");
        Object token = request.getSession().getAttribute("token");//模拟检查 token 过程
        if (token == null) {
            System.out.println("验证失败");
            return false;//返回 false 表示不允许访问
        } else {
            System.out.println("验证通过");
            return true;//返回 true 表示允许访问
        }
    }
}
