package org.giccqer.consumer.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.giccqer.consumer.dto.ConsumerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 使用OpenFeign进行通信时,需要调用该接口.
 * <p>需添加@FeignClient注解进行声明,注解中必须要添加一个name或value属性(一般为name) 声明注册中心提供者的名称</p>
 * <p>contextId属性表明了该接口实例注入IOC容器时的对象名.url属性表示发送请求的具体地址,若存在,
 * 则feign会根据该地址向提供者发送请求而不是从注册中心获取提供者的地址,因此在生产阶段记得删除该属性</p>
 */
@FeignClient(name = "CLIENT-PROVIDER", contextId = "feignApi", url = "http://localhost:8080")
//@FeignClient(name = "CLIENT-PROVIDER", contextId = "feignApi")
public interface FeignApi {
    /**
     * 注解@GetMapping内的网址为从提供者获取的页面的地址,用get方法.
     * <p>consumerInfo变量值会替换{info}字符串</p>
     *
     * @param consumerInfo 调用该方法后,传递给提供者的参数
     * @return 返回给controller层的字符串, 即提供者传递的结果
     */
    @GetMapping("/feign-str/{info}")
    String sendStrToProvider(@PathVariable("info") String consumerInfo);

    /**
     * 从提供者获取用户信息列表的api.
     *
     * @return 返回给controller层的信息列表
     */
    @GetMapping("/feign-consumers-list")
    List<ConsumerDto> getConsumersInfoList();

    /**
     * 向提供者传递post请求的api.
     * <p>consumes属性表明以何种数据类型传递给提供者,默认就是 application/json ,可以不写</p>
     *
     * @param consumer 从controller层中获取的请求体参数,用ConsumerDto封装
     * @return 返回给controller层的字符串, 即提供者传递的结果
     */
    @PostMapping(value = "/feign-receive-post-dto", consumes = "application/json")
    String sendDtoToProvider(ConsumerDto consumer);

    /**
     * 接收来自前端由dto封装的get请求参数,将其转化为问号?形式的请求参数后缀后发送给提供者.
     *
     * <p>必须添加 @SpringQueryMap 注解,该注解用于将待传输的dto数据转化为get方法的url请求后缀(问号形式)</p>
     *
     * @param consumer 从controller层中获取的请求体参数,会被转化为url参数后缀
     * @return 返回给controller层的字符串, 即提供者传递的结果
     */
    @GetMapping("/feign-receive-get-dto")
    String sendDtoToProvider2(@SpringQueryMap ConsumerDto consumer);

    /**
     * 提供一个固定返回错误响应体的api.
     *
     * @return 提供者返回的字符串消息
     */
    @GetMapping("/feign-error-page")
    String getErrorPageInfo();

    /**
     * 访问该方法大概率需要一定的时间才能从提供者那里得到响应.
     *
     * @param refreshCount 前端页面刷新次数
     * @return 返回给前端的字符串
     */
    @GetMapping("/feign-delay-info/{refresh-count}")
    String getDelayedInfo(@PathVariable("refresh-count") Integer refreshCount);

    /**
     * 向提供者发送数字,提供者会根据数字判断是否返回正常消息.
     * <p>需要加入 @CircuitBreaker 注解声明为该方法添加熔断器,其 name 属性为该熔断器的名字</p>
     * <p>fallbackMethod 属性为当该方法调用失败(出现异常或触发熔断时)需要调用的备选方法的名字</p>
     * <p>该方法要与本方法处于同一个接口中,用 default 关键字声明为默认实现的方法</p>
     *
     * @param number 从前端接收到的随机数
     * @return 返回给前端的字符串, 只有50%能执行该语句
     */
    @GetMapping("/feign-allowed-number")
    @CircuitBreaker(name = "sendRandomNumberService", fallbackMethod = "sendRandomNumberFallback")
    String sendRandomNumber(@RequestParam("number") Integer number);

    /**
     * 当sendRandomNumber 方法调用失败时,会调用该方法.
     * <p>该方法的返回值要与原方法相同,形参需要在原方法的基础上添加一个 Throwable 类型的形参,用来指明错误类型</p>
     * <p>该方法必须要与原方法处于同一个接口中</p>
     * <p>如果不写该方法,则消费者会正常向网页用户抛出异常消息(一般为500)</p>
     *
     * @param number 从前端接收到的随机数
     * @param e      出现异常的类型,可以用来打印异常信息
     * @return 默认返回给前端的字符串
     */
    default String sendRandomNumberFallback(Integer number, Throwable e) {
        return "😵请求已被阻断,本条消息来自于熔断器😵,你输入的数字是:" + number + "<br>错误消息: " + e.getMessage();
    }

    /**
     * 向提供者发送请求获取消息.
     * <p>@RateLimiter 注解表明该该方法使用限流器,其属性与断路器的相同,只不过当某些请求超时时抛出异常并调用 fallbackMethod 所提供的方法</p>
     *
     * @return 返回给前端的字符串
     */
    @GetMapping("/feign-current-limiting-info")
    @RateLimiter(name = "getCurrentLimitingInfoService", fallbackMethod = "getCurrentLimitingInfoServiceFallback")
    String getCurrentLimitingInfo();

    /**
     * 同断路器,提供当发生异常时所采取的备选方法.
     *
     * @param e 异常类型
     * @return 返回给前端的字符串
     */
    default String getCurrentLimitingInfoServiceFallback(Throwable e) {
        return "🤯🤯🤯请勿刷屏,您发送请求的次数太多了!错误消息: " + e.getMessage();
    }
}
