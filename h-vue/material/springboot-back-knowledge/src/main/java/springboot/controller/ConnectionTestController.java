package springboot.controller;

import org.springframework.web.bind.annotation.*;
import springboot.dto.LoginDataDto;
import springboot.dto.MessageDto;

/**
 * 用于初步前后端连接测试的控制器.
 * <p>可以在类上使用  {@code @CrossOrigin("允许的网址:端口号")}注解声明该类中的所有方法都可以被跨域访问</p>
 */
@RestController
public class ConnectionTestController {

    /**
     * 用于测试前后端是否能连通的hello页面.
     * <p>如果成功,后端会打印从前端返回的json数据,前端也能打印从后端返回的字符串数据</p>
     * <p>要优先解决跨域问题,最简单的做法是加入 {@code @CrossOrigin("允许的网址:端口号")}注解</p>
     *
     * @param param 前端请求的参数(json)
     * @return 一个纯字符串数据(data)
     */
    @GetMapping("/hello")
    @CrossOrigin("http://localhost")//默认html请求的端口号就是80,但有些浏览不会在请求时带上80端口号所以不写
    public String helloPage(@RequestParam(value = "parameter", required = false) String param) {
        if (param != null) System.out.println(param);
        return " 后端 返回纯字符串数据:你好vue!";
    }

    /**
     * 模拟账号密码登录测试.
     * <p>{@code @RequestBody} 方法参数注解声明了使用什么类封装从前端返回的数据,该类中的属性名要与前端json中的
     * 属性名称一致,只有该注解才可以处理从前端请求体中返回的json数据</p>
     * <p>如果想使用 {@code @RequestParam("属性名")} 获取单个属性值,或是什么注解都不想使用(这种情况会
     * 自动识别参数类中的字段名并与前端数据属性匹配),那么返回的请求头的 Content-Type 属性要声明为
     * application/x-www-form-urlencoded 或 multipart/form-data,这需要在前端进行进一步处理</p>
     *
     * @param user 用 {@code LoginMessageFromVue} 封装的从前端获取到的json数据
     * @return 用 {@code LoginMessageToVue} 封装并返回给前端的json数据
     */
    @PostMapping("/login_demo")
    public MessageDto getUserFromVue(@RequestBody LoginDataDto user) {
        if (user != null) System.out.println("接收到的账号密码类为: " + user);
        return new MessageDto("坤坤", "鸡你美不美");
    }
}
