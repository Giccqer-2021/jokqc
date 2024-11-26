# gateway网关

## 网关的搭建

1. 网关的作用在于将从客户端或网页访问的请求加以区分匹配并转发至设置好的的路径或端口号中.在顶级父项目中创建子项目  [c-gateway](material\springcloud-knowledge\c-gateway) ,其<groupId>标签如下:
   ```xml
   <groupId>org.giccqer.gateway</groupId>
   ```

   网关的本质相当于一个eureka客户端,在 [pom.xml](material\springcloud-knowledge\c-gateway\pom.xml) 中导入相关的依赖
   ```xml
   <dependencies>
       <dependency><!--Springboot依赖,注意不要使用Springboot-web-->
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter</artifactId>
       </dependency>
       <dependency><!--eureka客户端依赖-->
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
       </dependency>
       <dependency><!--springcloud网关依赖-->
           <groupId>org.springframework.cloud</groupId>
           <artifactId>spring-cloud-starter-gateway</artifactId>
       </dependency>
   </dependencies>
   ```

2. 在 resources 文件中创建  [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 配置,写入以下配置(重点在于 spring.cloud.gateway.routes 中的路由配置):
   ```yml
   spring:
     main:
       #NONE:不启动内嵌的服务器,也不执行web应用的配置
       #REACTIVE:启动内嵌的响应式服务器,适用于响应式(WebFlux)应用
       #SERVLET:启动内嵌的Servlet容器,适用于基于Servlet的应用(如Spring MVC)
       web-application-type: reactive #可选配置,如果你导入了 Springboot-web 包但不想换掉它则写上这个
     application:
       name: GATEWAY
     cloud:
       # 配置网关
       gateway:
         routes: #配置路由,即地址和端口的映射
           - id: send-to-apifox #路由的id,需要全局唯一,本例配置了将本机地址路由到外网的方法
             uri: https://echo.apifox.com #转换到的目标地址,匹配到 apifox 端口测试网站中
             predicates: #断言,要匹配的地址
               - Path=/send-to-apifox/** #一个*代表一个路径,两个*代表多个路径,路径以 / 隔开
             filters:
             #重写请求地址,使用了正则表达式重新将请求参数置入新地址中
               - RewritePath=/send-to-apifox(?<segment>.*), /get$\{segment} 
   server:
     port: 8100 #端口号,设置得大一点便于区分
   eureka:
     instance:
       hostname: localhost
       instance-id: GATEWAY
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka
   management:
     endpoints:
       web:
         exposure:
           include: info
     info:
       env:
         enabled: true
   info:
     app:
       name: GATEWAY
       status: GATEWAY网关运转良好
   ```
   在 [GatewayStarter.java](material\springcloud-knowledge\c-gateway\src\main\java\org\giccqer\gateway\GatewayStarter.java) 中写入启动该网关的主方法:
   ```java
   @SpringBootApplication
   public class GatewayStarter {
       public static void main(String[] args) {
           SpringApplication.run(GatewayStarter.class, args);
       }
   }
   ```

   启动该主方法(保存的启动项配置名为 c-GatewayStarter),不必启动其他实例,访问网址 http://localhost:8100/send-to-apifox?param1=HelloJava&param2=HiJavaScript ,观察是否能从外网正确获取路由消息

3. 路由本地消费者的地址:在 [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 中,加入以下路由配置:

   ```yml
   spring:
     cloud:
       gateway:
         routes:
   		- id: provider-hello #匹配到本机 provider 提供者中,访问hello方法
             uri: http://localhost:8080
             predicates:
               - Path=/provide-hello-info,/
   
           - id: provider-str #匹配到本机 provider 提供者中,访问需要参数的方法
             uri: http://localhost:8080
             predicates:
               - Path=/provide-str/{info}
             filters:
               - StripPrefix=1 #删除请求路径中第一个路径参数,注意:该删除功能无法识别地址中的?问号请求参数
               - PrefixPath=/feign-str #额外增加的前缀
   ```

   依次启动提供者 [b1-provider](material\springcloud-knowledge\b-eureka-client\b1-provider) 和网关,访问网址: http://localhost:8100/ , http://localhost:8100/provide-hello-info , http://localhost:8100/provide-str/HelloJava ,观察这些网址能否正确映射提供者所对应的地址

## 使用注册中心获取提供者实例

### 配置特定的实例映射

在路由中配置实例映射的关键在于修改路由器中 uri 属性的配置,将其改为以 lb:// 开头以提供者实例名称为"域名"的地址,如:
```yml
uri: lb://CLIENT-PROVIDER
```

完整的配置如下:在网关的 [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 中添加:
```yml
spring:
  cloud:
    gateway:
      routes:
        - id: provider-hello-by-server
          #匹配到注册中心中名为 CLIENT-PROVIDER 提供者的服务中,可能会对应多个提供者,使用负载均衡策略选择其中一个提供者
          uri: lb://CLIENT-PROVIDER
          predicates:
            - Path=/provide-hello-by-server
          filters:
            - StripPrefix=1
```

**依次** 启动服务端,提供者,网关,访问网址 http://localhost:8100/provide-hello-by-server ,观察是否能正常输出内容.其中的服务中心面板的网址为 http://localhost:8761/ 
然后再开启多个提供者实例(这里为 b1-MultiProviderStarter1,b1-MultiProviderStarter2,b1-MultiProviderStarter3),等待一段时间,然后打开网址 http://localhost:8100/provide-hello-by-server 并不停刷新,观察页面中输出的提供者实例名的变化
之后再随机关闭几个提供者实例(要保留一个),等待一段时间后访问 http://localhost:8100/provide-hello-by-server ,观察注册中心和网关是否能正确排除关闭了的提供则

### 配置启用默认实例映射

在 [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 中添加以下配置:
```yml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启服务发现,开启后将自动识别特定请求地址请求中包含的实例名,并在注册中心寻找目标实例的url
          lower-case-service-id: true #请求地址中的实例名若是小写,则是否将其视为大写
```

之后便可以使用以下方式访问位于注册中心客户端的网址了:

> http://网关的url/在服务中心注册的客户端名称/**

访问 http://localhost:8100/client-provider , http://localhost:8100/client-provider/provide-hello-info , http://localhost:8100/client-provider/feign-str/hahahahahahahaha 验证结果是否正确

## 路由匹配(断言)和过滤器配置

### 特定实例的断言和过滤

路由中的两个重要的属性,一个为 predicates 断言,用于匹配哪个网址需要进行转换,另一个为 filters 过滤器,用于将原网址转化为想要匹配到的网址.以下为配置示例

在提供者工程中的 [InfoProviderController.java](material\springcloud-knowledge\b-eureka-client\b1-provider\src\main\java\org\giccqer\provider\controller\InfoProviderController.java) 添加以下访问方法用来测试:
```java
@GetMapping("/get-user")
public String getUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (authorization == null) authorization = "无";
    return "<p>提供者已接收到用户消息,用户名: " + username + " ,密码: " + password + " ,你的访问令牌是: " + authorization + "</p>";
}
```

可以使用地址 http://localhost:8100/client-provider/get-user?username=Quixote&password=123456 测试该网址是否能够被访问

在网关的 [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 配置中,添加如下配置将外部地址路由于此

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: gateway-predicate #用于测试断言(即路径匹配规则)和局部过滤器的配置
          uri: lb://CLIENT-PROVIDER
          predicates:
            - Path=/gateway-predicate/**
            #必须要有 username=Ishmael 请求参数,等号及后面的内容可以不写,等号后面的内容可以匹配正则表达式
            - Query=username,Ishmael 
            - Query=password,654321 #必须要有 password=654321
            - Method=GET #支持的请求方法
            - After=2024-11-23T07:36:00.000+08:00[Asia/Shanghai] #什么时间之后可以访问该地址
            - Before=2025-11-23T07:36:00.000+08:00[Asia/Shanghai] #什么时间之前可以访问该地址
          filters:
            - RewritePath=/gateway-predicate(?<segment>.*), /get-user$\{segment} #别问这是啥,我也不懂
            - AddRequestHeader=Authorization,Ishmae #添加请求头
```

**依次**启动服务器,提供者和网关,访问如下地址 http://localhost:8100/gateway-predicate?username=Ishmael&password=654321 查看是否能够路由成功,观察提供者是否获取到了正确的令牌

同时,也可以尝试密码错误的网址如 http://localhost:8100/gateway-predicate?username=Ishmael&password=123456 查看该网址是否被拦截(404)

### 全局默认过滤器配置

你可以配置让任何一个发送到网关的请求都执行该过滤方法,在 [application.yml](material\springcloud-knowledge\c-gateway\src\main\resources\application.yml) 中添加配置:
```yml
spring:
  cloud:
    gateway:
      default-filters: #默认过滤器,优先级最低
        - AddResponseHeader=DefaultInfo,this-from-springcloud #添加响应头
```

重新访问 http://localhost:8100/gateway-predicate?username=Ishmael&password=654321 在网页中打开审查元素-网络,刷新页面,查看返回的响应头中是否具备 DefaultInfo 属性

###  predicates 断言的详细配置列表

|          断言名          |                     描述                     |
| :----------------------: | :------------------------------------------: |
|           Path           |              匹配请求的URI路径               |
|           Host           |               匹配请求的主机名               |
|          Method          |              匹配请求的HTTP方法              |
|          Query           |              匹配请求的查询参数              |
|          Header          |               匹配请求的头信息               |
|          Cookie          |               匹配请求的Cookie               |
|        RemoteAddr        |            匹配请求的远程地址(IP)            |
|          Weight          | 根据权重随机选择路由(通常与其他断言结合使用) |
| CloudFoundryRouteService |   针对Cloud Foundry的特定路由服务(不常用)    |
|          After           |   匹配在指定时间之后的请求(基于服务器时间)   |
|          Before          |   匹配在指定时间之前的请求(基于服务器时间)   |
|         Between          |  匹配在指定时间范围内的请求(基于服务器时间)  |

### filters 过滤器的详细配置列表:

|       过滤器名       |                       描述                       |
| :------------------: | :----------------------------------------------: |
|   AddRequestHeader   |           向请求中添加一个或多个HTTP头           |
| AddRequestParameter  |          向请求中添加一个或多个查询参数          |
|  AddResponseHeader   |           向响应中添加一个或多个HTTP头           |
| DedupeResponseHeader |              去除响应中的重复HTTP头              |
|      PrefixPath      |               在请求路径前添加前缀               |
|       SetPath        |               设置请求路径为指定值               |
|     RewritePath      |            使用正则表达式重写请求路径            |
|     StripPrefix      |         去除请求路径的一个或多个前缀部分         |
|        Retry         |                 在请求失败时重试                 |
|    CircuitBreaker    | 提供断路器功能，防止下游服务故障导致上游服务崩溃 |
|  RequestRateLimiter  |                   限制请求速率                   |
|  ModifyResponseBody  |                    修改响应体                    |
|      RedirectTo      |              将请求重定向到其他URL               |

### 自定义全局过滤器

1. 在网关工程中的 filter 包下,创建 [PerFilter.java](material\springcloud-knowledge\c-gateway\src\main\java\org\giccqer\gateway\filter\PerFilter.java) ,继承 GlobalFilter 类并重写filter()方法,打印原请求网址:
   ```java
   @Component
   @Order(0) //@Order()注解中的值决定了它的优先级,其值越小优先级越高(越先执行).你也可以通过继承Ordered接口并重写 getOrder()方法来设置优先级
   public class PerFilter implements GlobalFilter {
       @Override
       public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
           ServerHttpRequest request = exchange.getRequest();
           //ServerHttpResponse response = exchange.getResponse();
           String url = request.getURI().toString();
           String method = request.getMethod().name();
           System.out.println("已接收请求,原请求路径: " + url + " ,请求方式: " + method);
           return chain.filter(exchange); //责任链向下传递
       }
   }
   ```

   再创建 [PostFilter.java](material\springcloud-knowledge\c-gateway\src\main\java\org\giccqer\gateway\filter\PostFilter.java) ,打印经过其他过滤器过滤后的请求地址:
   ```java
   @Component
   @Order(10)
   public class PostFilter implements GlobalFilter {
       @Override
       public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
           ServerHttpRequest request = exchange.getRequest();
           String url = request.getURI().toString();
           System.out.println("请求被过滤,现请求路径: " + url);
           return chain.filter(exchange);
       }
   }
   ```

   开启提供者和网关(也可以开启服务器),随便访问一个网址(如 http://localhost:8100/ ),观察控制台对这些网址的过滤是否正确.这些代码将大大增强网关的可维护性

## 自定义网关异常处理器

在 handler 增加一个异常处理组件 [ExceptionFilter.java](material\springcloud-knowledge\c-gateway\src\main\java\org\giccqer\gateway\handler\ExceptionFilter.java) 即可:
```java
@Order(-1)
@Component
public class ExceptionFilter implements ErrorWebExceptionHandler {
    @Override //自定义的网关异常处理逻辑,基本上是固定写法,照写便是.
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() ->
                {
                    int stateCode; //错误代码
                    String message; //错误信息
                    try {
                        //从错误信息中获取前三个字符作为状态码
                        stateCode = Integer.parseInt(ex.getMessage().substring(0, 3));
                        //从错误信息中获取从第四个字符到倒数第一个字符之间的字符串作为错误信息,同时去除双引号
                        message = ex.getMessage().substring(4).replace("\"", "");
                    } catch (NumberFormatException e) {
                        stateCode = 500;
                        message = "Unknown";
                    }
                    response.setStatusCode(HttpStatus.valueOf(stateCode)); //设置响应体状态码
                    return exchange.getResponse().bufferFactory().wrap(("{\"错误代码\":\"" + stateCode + "\",\"错误信息\":\"" + message + "\"}").getBytes());
                })
        );
    }
}
```

启动网关,访问一个404界面(如 http://localhost:8100/error-page ),观察网页输出结果.同理,可以试一试引发各种错误,验证该异常处理器的效果

