package org.giccqer.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 优先级较高(最先执行)的过滤器,用来打印原请求网址.
 * <p>@Order() 注解中的值决定了它的优先级,其值越小优先级越高</p>
 */
@Component
@Order(0) //同理,你也可以通过继承 Ordered 接口并重写 getOrder() 方法来设置优先级
public class PerFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //ServerHttpResponse response = exchange.getResponse();
        String url = request.getURI().toString();
        String method = request.getMethod().name();
        System.out.println("已接收请求,原请求路径: " + url + " ,请求方式: " + method);
        return chain.filter(exchange); //责任链向下传递
    }
}

