package org.giccqer.consumer.controller;

import org.giccqer.consumer.feign.FeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoConsumerController {
    @Autowired
    private FeignApi feignApi;

    @GetMapping("/hello-info")
    public String getHelloFromProvider() {
        return "<p>消费者已从提供者得到消息: " + feignApi.getHelloFromProvider() + "</p>";
    }
}
