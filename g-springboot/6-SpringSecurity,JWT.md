# [SpringSecurit](https://spring.io/projects/spring-security),JWT

*本说明使用工程 [idea-springsecurity知识](material\idea-springsecurity-knowledge.bat)* 

## 起步(Gradle项目):

添加依赖:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

按照第一章的方法添加Controller,启动服务器并访问 http://localhost:8080/hello (注:本次不配置端口号和网址前缀),跳转至登陆页面,账户名为 user ,密码会于控制台内生成(类似uuid),输入账号密码后将会跳转,再次访问 hello 页面将无需再次输入.注:可以在浏览器中打开该网页的审查元素,在应用一栏中检查 cookie ,可发现cookie和session被创建了

## 自定义用户与密码

1. 最朴素的方法:使用 [application.yml](material\springsecurity-knowledge\src\main\resources\application.yml) 配置文件配置默认的用户名和密码,仅能配置一个
   ```yml
   spring:
     security:
       user:
         name: rnfmabj
         password: 2.71828
   ```

   该方法会随着使用自定义的服务层配置而失效,不推荐使用

2. 在 test 文件夹中创建  [CryptTest.java](material\springsecurity-knowledge\src\test\java\springboot\CryptTest.java) 测试文件,验证其加密方式并生成一些密码样本

   ```java
   public class CryptTest {
       @Test
       public void printTest() {
           String password = "123456";//原始密码,最好别定义成这样
           BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();//加密器
           String encode1 = bCryptPasswordEncoder.encode(password);//第一次加密
           System.out.println("第一次加密后的密码: " + encode1);
           String encode2 = bCryptPasswordEncoder.encode(password);//第二次加密,与上一次生成的密码不同
           System.out.println("第二次加密后的密码: " + encode2);
           //验证密码是否匹配,输出 true
           System.out.println("第一次加密后的密码解密: " + bCryptPasswordEncoder.matches(password, encode1));
           //验证第二次加密后的密码是否匹配,输出 true ,即使加密后生成的密码不同也可被匹配
           System.out.println("第二次加密后的密码解密: " + bCryptPasswordEncoder.matches(password, encode2));
       }
   }
   ```

3. 在 config 中创建配置类  [EncoderConfig.java](material\springsecurity-knowledge\src\main\java\springboot\config\EncoderConfig.java) ,配置密码加密方式:

   ```java
   @Configuration
   public class EncoderConfig {
       @Bean
       public PasswordEncoder passwordEncoder() {
           return new BCryptPasswordEncoder();
       }
   }
   ```

4. 创建 entity 文件夹,在该文件夹中创建类似于 entity 的数据实例化类  [UserEntity.java](material\springsecurity-knowledge\src\main\java\springboot\entity\UserEntity.java) ,该类必须继承 UserDetails 接口且重写 getAuthorities() , getPassword() 和 getUsername() 方法:
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class UserEntity implements UserDetails {
       private Integer id;
       private String name; //username 与 password 字段在命名时要避免其 getter方法 与下方重写的方法混淆
       private String userPassword;
       private List<String> PermissionList; //角色与权限表.通常需使用数据库联表查询,并用 List<权限封装类> 封装数据
       @Override //重写的方法,返回的对象声明了以何种方法来定义用户权限.
       public Collection<? extends GrantedAuthority> getAuthorities() {
           Collection<GrantedAuthority> grantedAuthorityArrayList = new ArrayList<>();//定义权限集合
           //固定写法,最后将相关的权限字符串传入即可
           PermissionList.forEach(permission -> grantedAuthorityArrayList.add(new SimpleGrantedAuthority(permission)));
           return grantedAuthorityArrayList;
       }
       @Override //重写的方法,声明该用户的密码.
       public String getPassword() {
           return this.userPassword;
       }
       @Override //重写的方法,声明该用户的用户名.
       public String getUsername() {
           return this.name;
       }
   }
   ```

5. 创建相关的数据表,配置并连接好数据库,导入相关依赖并创建持久层进行关联.这里使用枚举类来模拟从数据库查询到的数据,在 entity 包下创建  [UserExamples.java](material\springsecurity-knowledge\src\main\java\springboot\entity\UserExamples.java) :
   ```java
   @Getter
   public enum UserExamples {
       ROOT(new UserEntity(1, "root", "$2a$10$0cMLcvgvmq7tSa0.l4LuCeTSuXbTNhcl7oW6jwP7gt5uDg.wyakHy", List.of("admin"))), //密码 123456
       USER1(new UserEntity(2, "Ross", "$2a$10$pjjNjL.ss0ednp1iT7fr8OazKug9QT5N8pPXzeRP6osVC6mx0KB/u", List.of("user:view"))), //密码 654321
       USER2(new UserEntity(3, "Julie", "$2a$10$6hHtN.XaJ7Rwe1ibOVlfeu5g.49SSh/fA2rU65Ux28JRHKCJxUqFq", List.of("user:view", "user:add", "user:edit"))), //密码 114514
       USER3(new UserEntity(4, "Gloria", "$2a$10$BzFxW4rpd/kSLbJohT9SLuGxluurOg/J8nl2hTTHy6HAgHNGFntK6", List.of())); //密码 000000
       private final UserEntity userEntity;
       UserExamples(UserEntity userEntity) {
           this.userEntity = userEntity;
       }
   }
   ```

6. 在 service 中创建相关的服务层接口专门用于  [UserService.java](material\springsecurity-knowledge\src\main\java\springboot\service\UserService.java) 用户密码验证,要继承 UserDetailsService 接口
   ```java
   public interface UserService extends UserDetailsService {
   }
   ```

7. 在 service.impl 包中实现该接口,同时要实现 UserDetailsService 中的 loadUserByUsername() 方法,实现类为  [UserServiceImpl.java](material\springsecurity-knowledge\src\main\java\springboot\service\impl\UserServiceImpl.java) :
   ```java
   @Service
   public class UserServiceImpl implements UserService {
       @Override
       public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
           for (UserExamples example : UserExamples.values()) { //生产环境中请从数据库中查询
               UserEntity userEntity = example.getUserEntity();
               if (username.equals(userEntity.getUsername())) {
                   return userEntity;
               }
           }
           throw new UsernameNotFoundException("用户不存在"); //没查到用户时抛出异常
       }
   }
   ```

8. 重启服务器,访问 http://localhost:8080/hello ,输入自定义用户之一的用户名和密码,验证是否能够正常登录.

## 用户权限配置

1. 同上,在  [UserEntity.java](material\springsecurity-knowledge\src\main\java\springboot\entity\UserEntity.java) 实体类中定义了权限数组并重写了 getAuthorities() 方法后,向数据表中写入相关权限数据(这里以  [UserExamples.java](material\springsecurity-knowledge\src\main\java\springboot\entity\UserExamples.java) 枚举类用作模拟), 然后再主方法或配置类上添加注解:
   ```java
   @EnableMethodSecurity
   ```

2. 在 controller 包中创建  [DefaultController.java](material\springsecurity-knowledge\src\main\java\springboot\controller\DefaultController.java) 类用来模拟数据库操作,在需要权限控制的方法上添加 @PreAuthorize 注解,其中的 value值 需使用 SpEL 表达式声明权限控制:
   ```java
   @RestController
   @RequestMapping("/data")
   public class LimitsController {
       //hasAnyAuthority表示拥有任意之一的权限修饰符即可访问该方法
       @PreAuthorize("hasAnyAuthority('admin','user:add')")
       @RequestMapping("/add")
       public String dataAdd() {
           return "<p>成功执行 增加 数据命令</p>";
       }
       @PreAuthorize("hasAnyAuthority('admin','user:delete')")
       @RequestMapping("/delete")
       public String deleteData() {
           return "<p>成功执行 删除 数据命令</p>";
       }
       @PreAuthorize("hasAnyAuthority('admin','user:edit')")
       @RequestMapping("/edit")
       public String editData() {
           return "<p>成功执行 修改 数据命令</p>";
       }
       @PreAuthorize("hasAnyAuthority('admin','user:view')")
       @RequestMapping("/view")
       public String viewData() {
           return "<p>成功执行 查询 数据命令</p>";
       }
   }
   ```

3. 重启服务器,访问 http://localhost:8080/hello 并使用 Julie 用户(密码 114514 )登陆,然后测试以下网址是否可登录(该用户无权操作删除方法):

   > 增: http://localhost:8080/data/add
   >
   > 删: http://localhost:8080/data/delete
   >
   > 改: http://localhost:8080/data/edit
   >
   > 查: http://localhost:8080/data/view

4. 关于 @PreAuthorize 注解的value值:

   |                   value值                    |                   说明                   |
   | :------------------------------------------: | :--------------------------------------: |
   |          hasAuthority('角色:权限')           |  只有属于该角色且拥有该权限的用户可访问  |
   | hasAnyAuthority('角色1:权限1','角色2:权限2') | 拥有括号内任意角色权限标识符的用户可访问 |
   |             hasRole('ROLE_角色')             |        只有属于该角色的用户可访问        |
   |    hasAnyRole('ROLE_角色1','ROLE_角色2')     |    属于括号内任意一种角色的用户可访问    |

## (二选一)传统后端渲染自定义页面

1. 在resources文件夹下创建 static 静态资源文件夹和 templates 视图模板文件夹, static 文件夹中创建 script 文件夹并放入 [jquery](https://jquery.com/) 静态资源文件,在  [templates](material\springsecurity-knowledge\src\main\resources\templates) 放入登陆页面, 登陆成功页面和登陆失败页面,在 [build.gradle](material\springsecurity-knowledge\build.gradle) 中导入 thymeleaf 模板依赖
   ```groovy
   implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
   ```

   其中的登陆页面 [loginPage.html](material\springsecurity-knowledge\src\main\resources\templates\loginPage.html) :
   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>登录页面</title>
       <!--为了使静态资源能被成功放行，需要在static目录下创建script目录，以能够引入js文件-->
       <script type="text/javascript" th:src="@{/script/jquery-3.7.1.min.js}"></script>
   </head>
   <body>
   <p>请输入账号密码</p>
   <!--这里,username和password是SpringSecurity框架需要传入的属性名，不能随便更改-->
   <form method="post" action="/login">
       账号：<input type="text" name="username"> <br/>
       密码：<input type="password" name="password"> <br/>
       <!--用以防止跨域请求伪造攻击生成的验证码,以避免用户在非本页面执行登陆操作-->
       <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
       <input type="submit" value="登 录">
   </form>
   <hr>
   <button id="CustomButton">点我测试js代码</button>
   <script>
       $("#CustomButton").click(function() {alert("按钮被点击");});
   </script>
   </body>
   </html>
   ```

   登陆成功页面 [loginSuccess.html](material\springsecurity-knowledge\src\main\resources\templates\loginSuccess.html) :
   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>登陆成功</title>
   </head>
   <body>
   <!--如果登陆成功并将用户名写入attribute中，这里会显示用户名-->
   <p>欢迎用户: [[${user}]]</p>
   </body>
   </html>
   ```

   同时可以在 static 文件夹下创建 [error](material\springsecurity-knowledge\src\main\resources\static\error) 文件夹 自定义当发生特定的错误时向前端返回什么网页,命名为 错误代码.html ,其中:

   > 403:用户权限不足
   > 404:页面不存在
   > 405:请求方法错误

2. 在 controller 层中创建控制类 [LoginController.java](material\springsecurity-knowledge\src\main\java\springboot\controller\LoginController.java) 并创建控制方法,声明对这些模板的使用:
   ```java
   @Controller
   public class LoginController {
       @GetMapping(value = "/loginPage") //登录页面
       public String toLogin() {
           return "loginPage";
       }
       @GetMapping(value = "/") //登录成功页面
       public String loginSuccess(Model model) {
           //获取身份验证对象,固定写法照写便是
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String name = authentication.getName(); //用户名
           model.addAttribute("user", name); //写入 attribute , 在模板中通过 ${user} 展示
           return "loginSuccess";
       }
       @GetMapping(value = "/loginFailed") //登录失败页面
       public String loginFail() {
           return "loginFailed";
       }
   }
   ```

3. 在 config 文件夹中创建相关的页面登录配置类 [LoginConfig.java](material\springsecurity-knowledge\src\main\java\springboot\config\LoginConfig.java) : 
   ```java
   @Configuration
   public class LoginConfig {
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
           httpSecurity.formLogin(fromLogin ->
                   fromLogin.loginPage("/loginPage") //自定义跳转到的登陆页面,需要在controller中实现
                           .defaultSuccessUrl("/") //自定义登陆成功跳转页面,默认是"/"或上一次的请求页面
                           .failureUrl("/loginFailed") //自定义登录失败跳转页面
                           .loginProcessingUrl("/login")); //自定义的form表单提交地址
           httpSecurity.authorizeHttpRequests(authorizeHttpRequests ->
                   authorizeHttpRequests.
                           //放行的请求,访问这些不会验证账号密码,记得放行静态资源
                           requestMatchers("/loginPage", "/loginFailed", "/script/**").permitAll()
                           .anyRequest().authenticated());//其他的请求一律拦截并进行身份验证
           return httpSecurity.build();
       }
   }
   ```

4. 访问登录页面: http://localhost:8080/loginPage ,输入账号密码(可尝试错误的输入),然后观察页面输出

## JWT

*本说明使用工程 [idea-springsecurity前后端分离知识](material\idea-springsecurity-token-knowledge.bat)* 

1. 在 [build.gradle](material\springsecurity-token-knowledge\build.gradle) 中引入依赖(hutool-jwt依赖已包含hutool-json依赖):
   ```groovy
   implementation 'cn.hutool:hutool-jwt:5.8.28'
   ```

   除此之外,[hutool系列工具包](https://www.hutool.cn/)还包含[许多功能](https://blog.csdn.net/houxian1103/article/details/128721100),可以引入 hutool-all 依赖去使用所有hutool工具包
   
2. 在 test 文件夹下创建 [HutoolTest.java](material\springsecurity-token-knowledge\src\test\java\springboot\HutoolTest.java) ,运行该方法测试hutool对json数据的操控:
   ```java
   @Test
   public void JsonTest() {
       String jsonStr = JSONUtil.toJsonStr(UserExamples.USER2.getUserEntity());
       System.out.println("生成的json字符串: " + jsonStr);
       UserEntity jsonEntity = JSONUtil.toBean(jsonStr, UserEntity.class);
       System.out.println("json对象转化成的对象: " + jsonEntity);
   }
   ```

3. 关于 JWT :属于token令牌的一种,本质为经过编码的字符串,编码前由三部分组成:

   > **header 头部** :声明令牌使用的签名算法(HS256),令牌属性(JWT),一般是固定写法,使用 Base64 算法转化为字符串存储
   > 例如:{  "alg": "HS256",  "typ": "JWT" }
   > **payload 载荷** :需要传递的数据,默认为七个字段,可自定义私有字段,将用户数据信息放置于此,但由于该区域使用 Base64 编码信息易被破解所以不可以放置密码
   > **signature 签名** :对上述两部分进行不可逆的加密算法(无法解密出原文),需要使用私有密钥(盐值)加密,并将结果作为签名置入末尾.其作用为检验用户信息是否遭到篡改,这也是服务器在接收到token时所要做的第一件事

   jwt字符串的每一部分都会使用小数点 . 分隔,载荷数据越多载荷和签名部分便越长

3. 关于jwt载荷规范:
   ???
   
4. 使用 hutool-jwt 工具包将自定义的 json 字符作为载荷,并生成token,然后验签解密的方法(在测试类中进行):
   ```java
   @Test
   public void JWTStringTest() {
       //创建自定义的json字符串
       String jsonStr = JSONUtil.createObj().set("name", "小破孩").set("age", "八岁啦").set("gender", "男").toString();
       System.out.println("创建的json字符串为: " + jsonStr);
       String token = JWT.create().setPayload("user", jsonStr).setKey(SECRET.getBytes()).sign(); //加密签名
       System.out.println("生成的token: " + token);
       boolean verify = JWTUtil.verify(token, SECRET.getBytes()); //验签
       System.out.println("验证该token是否合法: " + verify);
       String getJson = (String) JWTUtil.parseToken(token).getPayload("user"); //解码
       System.out.println("从token中获取的json字符串为: " + getJson);
   }
   ```

   将一个对象直接封装成载荷(会被转化为json数据),生成token,然后验签解密并提取对象的方法:
   ```java
   @Test
   public void JWTObjectTest() {
       UserEntity userEntity = UserExamples.USER2.getUserEntity(); //实例化对象
       String token = JWTUtil.createToken(Map.of("user", userEntity), SECRET.getBytes()); //加密签名
       System.out.println("生成的对象token: " + token);
       boolean verify = JWTUtil.verify(token, SECRET.getBytes()); //验签
       System.out.println("验证该对象token是否合法: " + verify);
       String userJSON = JWTUtil.parseToken(token).getPayload("user").toString(); //解码
       System.out.println("从对象token中获取的json字符串为: " + userJSON);
       UserEntity getUser = JSONUtil.toBean(userJSON, UserEntity.class); //将json字符串转换成对象
       System.out.println("从json字符串中获取的对象为: " + getUser);
   }
   ```

## (二选一)前后端分离

*本说明使用工程 [idea-springsecurity前后端分离知识](material\idea-springsecurity-token-knowledge.bat)* 

1. 前后端分离的原理:前端使用vue(或nignx服务器)来处理页面的渲染,相关静态资源的调用,后端负责登陆控制,权限控制,数据传输等.后端只与前端使用json数据进行交互.在该交互模式下,不再需要传统的 session cookie 来验证登录状态,改为使用 jwt token 验证,token要写在发送请求的请求头中
2. 在 [build.gradle](material\springsecurity-token-knowledge\build.gradle) 导入相关的 redis 依赖:
   ```groovy
   implementation 'org.springframework.boot:spring-boot-starter-data-redis'
   ```

   并在 [application.yml](material\springsecurity-token-knowledge\src\main\resources\application.yml) 中配置redis:
   ```yml
   spring:
     data:
       redis:
         host: 127.0.0.1
         port: 6379
         database: 0
         password: 123456@abcdef #若没有设置密码则无需配置
   ```

3. 创建 message 包并创建  [UserInfo.java](material\springsecurity-token-knowledge\src\main\java\springboot\message\UserInfo.java) 类来封装传递的简单消息:

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class Notification {
       private String message;
   }
   ```

   创建 [Notification.java](material\springsecurity-token-knowledge\src\main\java\springboot\message\Notification.java) 用户信息类来封装用户实体类,要避免封装其中的密码.由于之后需要调用实体类 [UserEntity.java](material\springsecurity-token-knowledge\src\main\java\springboot\entity\UserEntity.java) 的 getAuthorities() 方法所以建议在该类中补写一个(如果直接使用原实体类封装消息可以在敏感字段上加入 @JsonIgnore 注解)
   ```java
   @Data
   @NoArgsConstructor
   public class UserInfo {
       private Integer id;
       private String name;
       private List<String> PermissionList;
       public UserInfo(UserEntity userEntity) {
           this.id = userEntity.getId();
           this.name = userEntity.getName();
           this.PermissionList = userEntity.getPermissionList();
       }
   }
   ```

4. 在 config 包中创建 [LoginConfig.java](material\springsecurity-token-knowledge\src\main\java\springboot\config\LoginConfig.java) 配置类,配置相关的过滤器,登陆成功失败未授权等情境的处理方法,该代码耦合度较高,在实际应用中请尽可能解耦
   ```java
   @Configuration
   public class LoginConfig {
       private static final String SECRET = "3Gdr0/h8Dd03=e3dAU"; //秘钥, 用于生成token的盐值,绝不可以泄露
       @Autowired
       private StringRedisTemplate redisTemplate; //redis的字符串数据处理与传输模板对象.
   	//与后端渲染不同,这里配置的"页面地址"不再从后端返回一个页面,而是返回一条json封装的消息
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
           httpSecurity.formLogin(fromLogin -> fromLogin
   /*自定义登陆页面,已失效,由于关闭了session验证方式所以所以在过滤器验证token合法后,所有非登陆类型的请求(或非post类型的登录请求)都会被默认"需要"使用账号密码再次登录才能访问,这里配置自定义的"登陆页面"以阻止后端发送默认的登陆页面再次验证登录,自定义"登录页面"只需发送文本消息即可注意:当发生 404,405 错误时,客户端页面都会直接跳转到这里*/
                           .loginPage("/loginPage")
                           .loginProcessingUrl("/login") //提交请求的地址,固定
                           .defaultSuccessUrl("/") //登陆成功(账号密码输入正确)后跳转的"页面"
                           .failureUrl("/loginFailed") //登陆失败(账号密码错误)后跳转的"页面"
                           // 成功登陆后,调用此方法,将token写入redis,并返回给前端
                           .successHandler((request, response, authentication) -> {
                               // 获取登陆时的认证信息,获得的实体类类型取决于在Service层的相关配置
                               UserEntity user = (UserEntity) authentication.getPrincipal();
                               //使用相关信息类重新封装上述登陆信息,避免密码等敏感信息被写入jwt载荷
                               UserInfo userInfo = new UserInfo(user);
                               String userJSON = JSONUtil.toJsonStr(userInfo);//将登陆信息类封装为json字符串
                               //生成token,需提供载荷名和加密秘钥
                               String token = JWTUtil.createToken(Map.of("user", userJSON), SECRET.getBytes());
                               redisTemplate.opsForValue().set("t" + user.getId(), token); //写入redis
                               writeJSONMessage(response, token);//自定义的方法,将token写入响应体中返回
                           })
                           .failureHandler((request, response, exception) -> //登陆失败后,调用此方法,返回错误消息
                                   writeJSONMessage(response, exception.getMessage())))
                   .csrf(AbstractHttpConfigurer::disable) //关闭csrf验证,使用token本身可以避免csrf攻击
                   .sessionManagement(sessionManagement ->//取消默认的session验证策略,改为无状态验证
                           sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(request ->//除了"登陆"页面,所有的页面都需要拦截,后端不再提供静态资源的访问
                           request.requestMatchers("/loginPage").permitAll().anyRequest().authenticated())
                   //自定义过滤器,用于验证token合法性,并设置认证信息.过滤器会在SpringSecurity默认方法进行验证拦截前执行
                   .addFilterAfter(new OncePerRequestFilter() {
                       @Override
                       protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                           //获取请求头中的Authorization属性字段(即token),该字段已由前端配置完毕
                           String token = request.getHeader("Authorization");
                           if (request.getRequestURI().equals("login")) { //如果是登陆请求,由于尚未生成token,直接放行
                               filterChain.doFilter(request, response);//向下传透放行
                           } else if (!StringUtils.hasText(token)) { //如果请求头中没有token字段,直接返回错误消息
                               writeJSONMessage(response, "请求token为空");
                           } else if (!JWTUtil.verify(token, SECRET.getBytes())) {//token验签未通过,直接返回错误消息
                               writeJSONMessage(response, "请求token非法");
                           } else {
                               JWT jwt = JWTUtil.parseToken(token); //解析token
                               String userJSON = jwt.getPayload("user").toString(); //获取token载荷中的用户信息类
                               //将用户信息转换为实体类对象
                               UserInfo userInfo = JSONUtil.toBean(userJSON, UserInfo.class);
                               //根据用户实体类对象中的id,查询redis数据库,比较token是否一致,若不一致则不通过
                               if (!token.equals(redisTemplate.opsForValue().get("t" + userInfo.getId()))) {
                                   writeJSONMessage(response, "请求token未通过");
                               } else { //通过验证,设置认证信息
   				//在SpringSecurity的上下文中放入认证信息，表示该用户曾经登陆过,否则每次访问后端请求都会索要账号和密码
   UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
   SecurityContextHolder.getContext().setAuthentication(authenticationToken); //认证信息放入上下文
                                   filterChain.doFilter(request, response); //向下传透放行
                               }
                           }
                       }
                   }, UsernamePasswordAuthenticationFilter.class)
                   //当发生 403 权限不足的错误时的处理办法,后端不再提供相应的页面,要自行传递消息配置
                   .exceptionHandling((exceptionHandling) -> {
                       exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                           writeJSONMessage(response, "访问权限不足");
                       });
                   });
           return httpSecurity.build();
       }
       //自定义的将信息封装入消息类并写入响应体的方法.
       private void writeJSONMessage(HttpServletResponse response, String message) {
           response.setContentType("application/json");//声明返回类型为json
           response.setCharacterEncoding("utf-8");//设置编码,避免中文乱码
           String json = JSONUtil.toJsonStr(new Notification(message));//Notification为自定义的简单消息传递类
           try (PrintWriter printWriter = response.getWriter()) {//其实不必写这么复杂,这只是种优雅的写法
               printWriter.write(json);
               printWriter.flush();
           } catch (IOException e) {
               throw new RuntimeException("json转换异常");
           }
       }
   }
   ```

5. 在 controller 包中创建 [LoginController.java](material\springsecurity-token-knowledge\src\main\java\springboot\controller\LoginController.java) 类来处理页面登录的相关的消息映射(loginPage,/,loginFailed等),这里不会再跳转到对应的"页面"中了而是直接返回相应的json消息

6. 启动服务端,并打开 postman 端口测试软件,创建 POST 登陆请求,url为 http://localhost:8080/login ,在**请求体**中加入账号密码:

   > username: Julie
   >
   > password: 114514

   发送访问请求并在响应体中获得对应的token,观察后端redis中存储的 token数据

7. 创建 GET 访问请求,url为 http://localhost:8080/hello ,在**请求头**中创建 **Authorization** 属性并写入之前得到的 token数据 (注意不要乱添加空格回车),发送请求观察返回的数据

8. 同理,添加 GET请求并在请求头中写入token,依次向以下url发送访问请求来测试控制权限:
   >增: http://localhost:8080/data/add
   >
   >删: http://localhost:8080/data/delete
   >
   >改: http://localhost:8080/data/edit
   >
   >查: http://localhost:8080/data/view