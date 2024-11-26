package springboot.filter;

import jakarta.servlet.*;

import java.io.IOException;

/**
 * 使用传统 servlet 方式的过滤器.
 * <p>可以使用配置类声明该配置并使其生效,或在本类添加 {@code @WebFilter} 并在主方法上声明 {@code @ServletComponentScan("要扫描的包")} 使其生效</p>
 */
//@WebFilter(urlPatterns = "/*")
public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Filter过滤器已检测到网页通过");//放行前要执行的代码
        chain.doFilter(request, response);//网页传透放行
    }
}
