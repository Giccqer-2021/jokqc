package org.giccqer.provider.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.giccqer.provider.dto.ProviderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class InfoProviderController {

    /**
     * 提供者的实例Id,读取自 application.yml 文件中的配置.
     */
    @Value("${eureka.instance.instance-id}")
    public String providerInstanceId;

    /**
     * 用来统计 sendCurrentLimitingInfo() 方法收到的总的请求次数.
     */
    public static int getCurrentLimitingRequestCount = 1;

    /**
     * 年轻人所创建的第一个视图层方法.
     *
     * @return 返回给客户端的字符串消息, 包含提供者的实例ID
     */
    @GetMapping({"/provide-hello-info", "/"})
    public String provideHelloInfo() { //就当这些文字是从数据库拿到的吧😎
        return "<p>😊这条消息来自Client Provider提供者客户端😊,提供者的实例ID是: " + providerInstanceId + "</p>";
    }

    /**
     * 测试消费者使用OpenFeign来访问该方法.
     *
     * @param info 消费者传递来的字符串参数
     * @return 返回给消费者的字符串消息
     */
    @GetMapping("/feign-str/{info}")
    public String sendStrToConsumer(@PathVariable("info") String info) {
        return "已接收到来自消费者的消息---" + info;
    }

    /**
     * 同上,只不是使用dto封装数据对象并进行通信.
     *
     * @return 返回给消费者的ProviderDto对象列表
     */
    @GetMapping("/feign-consumers-list")
    public List<ProviderDto> provideConsumerInfoList() {
        return List.of(new ProviderDto("张三", 18, 12345678901L),
                new ProviderDto("李四", 19, 12345678902L),
                new ProviderDto("王五", 20, 12345678903L));
    }

    /**
     * 同上,接收消费者使用feign传递来的post请求.
     *
     * @param consumer 使用ProviderDto对象封装其中的请求参数
     * @return 返回给消费者的字符串消息
     */
    @PostMapping("/feign-receive-post-dto")
    public String receivePostDto(@RequestBody ProviderDto consumer) {
        return "post方法已收到顾客信息---姓名" + consumer.getName() + "，年龄---" + consumer.getAge() + ", 电话---" + consumer.getPhone();
    }

    /**
     * 同上,不过是接收get方法传递来的参数,封装于ProviderDto中.
     *
     * @param consumer 封装好的ProviderDto对象
     * @return 向消费者返回的字符串消息
     */
    @GetMapping("/feign-receive-get-dto")
    public String receiveGetDto(ProviderDto consumer) {
        return "get方法已收到顾客信息---姓名" + consumer.getName() + "，年龄---" + consumer.getAge() + ", 电话---" + consumer.getPhone();
    }

    /**
     * 固定向消费者返回一个错误的响应体.
     *
     * @return 状态码403的响应体
     */
    @GetMapping("/feign-error-page")
    ResponseEntity<String> sendExceptionInfo() {
        return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body("禁止访问本网址");
    }

    /**
     * 根据前端发送的页面刷新次数,判断是否向消费者发送延迟消息.
     *
     * @param refreshCount 前端页面的刷新次数参数
     * @return 返回给消费者的字符串消息
     */
    @GetMapping("/feign-delay-info/{refresh-count}")
    public String sendDelayedInfo(@PathVariable("refresh-count") Integer refreshCount) {
        if (refreshCount % 3 != 0) { //每刷新三次页面就有一次不再延迟返回消息
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return "已发送延迟响应的消息,你的刷新次数为---" + refreshCount;
    }

    /**
     * 接收一个随机的数字,然后判断其值是否大于50,若是则正常返回,否则返回403.
     * <p>旨在模拟网络通信中可能会出现的随机故障</p>
     *
     * @param number   接收到的随机数字
     * @param response 返回的响应体,用于设置状态码
     * @return 返回的响应体内容
     */
    @GetMapping("/feign-allowed-number")
    public String getAllowedNumber(@RequestParam("number") Integer number, HttpServletResponse response) {
        if (number < 51) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "你输入的数字是" + number + ",其值不大于50,不允许访问!🤬🤬🤬";
        } else {
            return "你输入的数字是" + number + ",其值大于50,允许访问!😃😃😃";
        }
    }

    /**
     * 本方法用于测试消费者的限流器功能.
     *
     * @return 返回的响应体字符串, 包含了本方法一共被访问的次数消息
     */
    @GetMapping("/feign-current-limiting-info")
    public String sendCurrentLimitingInfo() {
        return "🎈这是来自提供者返回的消息,本方法已接收到的请求次数为🎈: " + getCurrentLimitingRequestCount++;
    }

    /**
     * 本方法用来测试网关地址对请求参数的匹配,由浏览器直接访问而并非提供给消费者.
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回给前端的数据
     */
    @GetMapping("/get-user")
    public String getUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) authorization = "无";
        return "<p>提供者已接收到用户消息,用户名: " + username + " ,密码: " + password + " ,你的访问令牌是: " + authorization + "</p>";
    }
}
