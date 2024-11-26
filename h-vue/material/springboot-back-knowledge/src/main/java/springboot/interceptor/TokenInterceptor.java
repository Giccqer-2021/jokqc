package springboot.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用于身份确认的拦截器,模拟 token 验证的过程.
 * <p>实际生产环境中需要通过redis数据库对拦截网页请求头中的token进行验证,这里省略了很多步骤</p>
 */
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 注意:浏览器进行ajax跨域请求前会发送一个OPTIONS类型的请求获取服务器支持的HTTP请求方式,该请求不能被拦截否则报错
        if (request.getMethod().equals("OPTIONS")) return true;
        String token = request.getHeader("Authorization"); //从请求头中获取 token
        if (token == null) { //如果没有token
            response.sendError(401, "用户身份未验证"); //401 未授权
            return false; //拦截
        } else if (!token.equals("rnfmabj")) { //如果token不正确
            response.sendError(402, "用户身份验证未通过");
            return false;
        }
        return true; //这里省略了验签,从token中获取用户名并从redis查询对应token的过程
    }
}