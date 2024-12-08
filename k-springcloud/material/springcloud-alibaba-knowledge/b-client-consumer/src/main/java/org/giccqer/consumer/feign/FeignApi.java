package org.giccqer.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PROVIDER")
public interface FeignApi {
    @GetMapping("/hello-info")
    String getHelloFromProvider();
}
