package org.giccqer.consumer.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentinelRequestIp implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        System.out.println("===网页的IP: " + httpServletRequest.getRemoteAddr());
        return "all";
    }
}
