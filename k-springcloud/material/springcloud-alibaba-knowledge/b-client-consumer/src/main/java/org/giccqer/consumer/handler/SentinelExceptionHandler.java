package org.giccqer.consumer.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String resourceName, BlockException exception) throws Exception {
        String msg;
        int status = 403;
        switch (exception) {
            case FlowException ignored -> msg = "请求被限流了";
            case ParamFlowException ignored -> msg = "请求被热点参数限流";
            case DegradeException ignored -> msg = "请求被降级了";
            case AuthorityException ignored -> {
                msg = "没有权限访问";
                status = 401;
            }
            case null, default -> {
                msg = "请求被拒绝";
            }
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.println("{\"状态码\": " + status + ", \"错误消息\": " + msg + "}");
        writer.flush();
        writer.close();
    }
}
