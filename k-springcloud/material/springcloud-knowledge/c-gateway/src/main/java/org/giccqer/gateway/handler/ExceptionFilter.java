package org.giccqer.gateway.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义的网关异常处理器.
 */
@Order(-1)
@Component
public class ExceptionFilter implements ErrorWebExceptionHandler {

    /**
     * 自定义的网关异常处理逻辑,基本上是固定写法,照写便是.
     * <p>Mono 类是Java Reactor库中的一个核心组件，主要用于处理包含零个或一个元素的异步序列‌</p>
     *
     * @param exchange 目前正在处理请求的过滤器对象
     * @param ex       发生的异常
     * @return Mono核心组件对象
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() ->
                {
                    int stateCode; //错误代码
                    String message; //错误信息
                    try {
                        stateCode = Integer.parseInt(ex.getMessage().substring(0, 3)); //从错误信息中获取前三个字符作为状态码
                        //从错误信息中获取从第四个字符到倒数第一个字符之间的字符串作为错误信息,同时去除双引号
                        message = ex.getMessage().substring(4).replace("\"", "");
                    } catch (NumberFormatException e) {
                        stateCode = 500;
                        message = "Unknown";
                    }
                    response.setStatusCode(HttpStatus.valueOf(stateCode)); //设置响应体状态码
                    return exchange.getResponse().bufferFactory().wrap(("{\"错误代码\":\"" + stateCode + "\",\"错误信息\":\"" + message + "\"}").getBytes());
                })
        );
    }
}
