package org.giccqer.consumer.controller;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.giccqer.consumer.dto.ConsumerDto;
import org.giccqer.consumer.feign.FeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class InfoConsumerController {
    /**
     * RestTemplate实例对象,已配置好.
     */
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 用来操作负载均衡器的对象.
     * <p>通过该对象可动态调整负载均衡策略,监控负载均衡情况,更新服务实例等,本例用于获取提供者的url</p>
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * FeignApi实例对象,提供各种服务器消息请求传递的api.
     */
    @Autowired
    private FeignApi feignApi;

    /**
     * 断路器注册器.
     * <p>主要用于获取名为 sendRandomNumberService 断路器实例并获取其所统计到的请求接收失败次数</p>
     */
    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * 注意:这是页面刷新次数而不是重连次数.
     */
    public static int refreshCount = 1;

    /**
     * 使用传统方法向提供者发起http请求.
     *
     * @return 返回给前端的字符串信息, 包含提供者的url
     */
    @GetMapping("/hello")
    public String helloInfo() {
        //提供者的url,CLIENT-PROVIDER为其在注册中心中的名字
        String helloProviderUrl = "http://CLIENT-PROVIDER/provide-hello-info";
        String resultFromProvider = restTemplate.getForObject(helloProviderUrl, String.class); //发送请求
        String providerUrl = loadBalancerClient.choose("CLIENT-PROVIDER").getUri().toString(); //获取提供者的url
        return "<p>这条消息来自Client Consumer消费者客户端</p>" + resultFromProvider + "<p>提供者的url: " + providerUrl + "</p>";
    }

    /**
     * 使用feign调用提供者所提供的方法.
     *
     * @return 返回给前端的字符串信息, 包含提供者的url
     */
    @GetMapping("/feign-hello")
    public String feignHello() {
        String resultFromProvider = feignApi.sendStrToProvider("你好我是消费者"); //直接调用feign api中的方法即可
        //同上,获取提供者的url.注意:由于注册中心未必会启动,因此需要进行非空判断
        ServiceInstance providerInstance = loadBalancerClient.choose("CLIENT-PROVIDER");
        String providerUrl;
        if (providerInstance != null) {
            providerUrl = providerInstance.getUri().toString();
        } else {
            providerUrl = "NULL";
        }
        return "<p>提供者返回的内容是: " + resultFromProvider + " ,提供者的url是: " + providerUrl + "</p>";
    }

    /**
     * 使用feign调用提供者所提供的方法,获取顾客信息列表.
     *
     * @return 返回给前端的列表对象.
     */
    @GetMapping("/feign-consumers-list")
    public String getConsumersList() {
        return "<p>从提供者得到的顾客信息列表: " + feignApi.getConsumersInfoList().toString() + "</p>";
    }

    /**
     * 在服务器内部消费者使用post方法向提供者传递请求.
     *
     * @return 返回给前端的结果
     */
    @GetMapping("/feign-post-send-dto")
    public String sendConsumerInfo() {
        return "<p>成功传输客户数据,并返回结果: " + feignApi.sendDtoToProvider(new ConsumerDto("小明", 18, 1234567890L)) + "</p>";
    }

    /**
     * 服务器内部会将dto对象转化为get请求参数(问号形式的),然后将请求发送给提供者.
     *
     * @return 向前端返回的结果
     */
    @GetMapping("/feign-get-send-dto")
    public String sendConsumerInfo2() {
        return "<p>成功传输客户数据,并返回结果: " + feignApi.sendDtoToProvider2(new ConsumerDto("王小美", 26, 7417417474741L)) + "</p>";
    }

    /**
     * 访问该方法后,服务器内部将发生错误.
     *
     * @return 返回给前端的结果
     */
    @GetMapping("/feign-error")
    public String getErrorInfo() {
        return "<p>提供者返回的异常消息是: " + feignApi.getErrorPageInfo() + "</p>";
    }

    /**
     * 访问该方法以获取从提供者那里提供的延时响应的消息.
     *
     * @return 返回给前端的结果
     */
    @GetMapping("/feign-delay")
    public String getDelayedInfo() {
        return "<p>提供者返回的延时消息是: " + feignApi.getDelayedInfo(refreshCount++) + "</p>";
    }

    /**
     * 生成一个 1~100 的随机数,然后将该数发送到提供者,并等待提供者返回消息.
     * <p>由于成功率只有50%,所以很可能触发熔断</p>
     *
     * @return 返回给前端的结果
     */
    @GetMapping("/feign-send-random-number")
    public String sendAllowedNumber() {
        //获取断路器实例并获取其统计到的请求失败率,通用写法,照写便是
        float failureRate = circuitBreakerRegistry.circuitBreaker("sendRandomNumberService").getMetrics().getFailureRate();
        int randomNumber = 1 + (int) (Math.random() * 100);
        return "<p>提供者返回的消息是:<br>" + feignApi.sendRandomNumber(randomNumber) + "<br>目前的请求失败率是:" + failureRate + "%</p>";
    }

    /**
     * 连续多次调用 feignApi.getCurrentLimitingInfo() 方法,制造流量峰值.
     * <p>用来测试限流器是否能够正常工作</p>
     *
     * @return 返回给前端的结果
     */
    @GetMapping("/feign-send-multiInfo")
    public String sendMultiInfo() {
        for (int count = 0; count < 4; count++) {
            feignApi.getCurrentLimitingInfo();
        }
        return "<p>一共发送了5次请求,最后一次请求提供者返回的消息是:<br>" + feignApi.getCurrentLimitingInfo() + "</p>";
    }
}
