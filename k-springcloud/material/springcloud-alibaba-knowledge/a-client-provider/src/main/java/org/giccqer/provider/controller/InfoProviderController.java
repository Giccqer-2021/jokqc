package org.giccqer.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoProviderController {
    @GetMapping("/hello-info")
    public String helloPage() {
        return "这是来自提供者返回的消息---你好啊,朋友😄";
    }
}
