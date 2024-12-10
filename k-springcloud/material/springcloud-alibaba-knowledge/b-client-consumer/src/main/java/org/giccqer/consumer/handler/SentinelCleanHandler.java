package org.giccqer.consumer.handler;

import com.alibaba.csp.sentinel.adapter.web.common.UrlCleaner;
import org.springframework.stereotype.Component;

@Component
public class SentinelCleanHandler implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        if (originUrl.equals("/get-configs")) {
            return "/get-faces";
        }
        return originUrl;
    }
}
