# [Axios](https://axios-http.com/zh/)网络连接与数据传输

## 外网连接测试

1. cmd指令下载 axios 模块依赖: npm i axios

2. 在 src 目录创建 utils 文件夹,创建 [request.js](material\vue-front-knowledge\src\utils\request.js) 网络连接工具模块,创建 doGet() 和 doPost() 方法并将二者导出:
   ```javascript
   import axios from "axios";//aixos作用类似于ajax,可以向后端发送数据
   //本方法具有返回值,必须加return.get方法没有请求体,params请求参数会被写入url地址的后面
   export const doGet = (url, params) => {
       return axios({
           method: 'get',
           url: url,
           params: params
       })
   }
   //post方法中data数据会被写入其请求体,也可以像get方法那样添加params请求参数写在url地址后面但没必要
   export const doPost = (url, data) => {
       return axios({
           method: 'post',
           url: url,
           data: data
       })
   }
   ```

3. 在测试模块 [AxiosOuterTest.vue](material\vue-front-knowledge\src\components\AxiosOuterTest.vue) 的<template>中添加用来发送请求的按钮(视为表单提交按钮):
   ```html
   <!-- 按下后会向指定的外网发生一些数据,分别使用get与post方法 -->
   <el-button type="primary" @click="getRequest">点此发送get请求进行外网连接测试</el-button><hr />
   <el-button type="primary" @click="postRequest">点此发送post请求进行外网连接测试</el-button>
   ```

   同时在<script>模块写入其对应的按钮点击事件:
   ```javascript
   import { doGet, doPost } from "../utils/request";//导入get,post方法(这个模块是自己写的)
   // 按钮绑定的向往外发送数据的方法,用doPost()返回的对象.then()中添加回调函数用来处理发送数据成功后要做的事(不成功则报错)
   // resp表示服务端返回的数据
   const getRequest = () => {
       doGet(
           //相当于 https://echo.apifox.com/get?q1=这是参数1&q2=这是参数2 ,该url为外网专门用以测试端口连接的网站网址
           'https://echo.apifox.com/get',
           {
               q1: "这是参数1",
               q2: "这是参数2"
           }
       ).then(resp => {
           console.log(resp.data)//控制台输出
           //对话框输出
           alert('get请求发送的请求参数为: q1-' + resp.data.args.q1 + '   q2-' + resp.data.args.q2)
       })
   }
   const postRequest = () => {
       doPost(
           'https://echo.apifox.com/post',//一般post方法不会在url中携带请求参数,但也可以这么做
           {
               d: "你好呀",
               dd: "打声招呼吧"
           }
       ).then(resp => {
           console.log(resp.data)
           //resp.data表示得到请求体数据
           alert('post请求发送请求体为:' + resp.data.data)
       })
   }
   ```
   
4. 测试运行页面,分别点击两个按钮,观察控制台及对话框输出

5. 同理,可以在 [request.js](material\vue-front-knowledge\src\utils\request.js) 新增以下方法(rest风格)用来操作后端数据库:

   |    方法    | 用途 | 该方法是否具有请求体 |
   | :--------: | :--: | :------------------: |
   |  doGet()   | 查询 |          否          |
   |  doPost()  | 新增 |          是          |
   |  doPut()   | 修改 |          是          |
   | doDelete() | 删除 |          否          |

   可以访问 [用于测试端口的网站](https://echo.apifox.cn/) 或 [提供各类软件api端口的网站](https://apifox.com/apihub/) 获取更多信息

## SpringBoot本地连接测试

*本说明需要前端工程同时与 [vue后端知识(默认后端工程)](material\idea-springboot-back-knowledge.bat) 同步运行*

### 跨域并连通

1. 解决http请求跨域问题:对于一个请求,如果它的 请求协议,请求地址(主机名)或请求的端口号 任意一项与被请求的不同,则该请求为跨域请求,出于安全考虑SpringBoot不允许跨域访问,因此需要开放后端跨域访问权限
   阅读以下内容前需创建并初始化完毕一个后端Springboot项目

2. 使用以下三种方法之一解决跨域问题:

   - 在 controller 层中的方法或类上添加注解:
     ```java
     @CrossOrigin("网址:端口号") //该url允许跨域访问该方法所映射的网络地址,可以应用于某个方法或某个视图层类
     ```

     在类上添加该注解则可以声明该类中的所有方法都可以被跨域访问
     
   - 在创建全局配置类 [CorsGlobalConfig.java](material\springboot-back-knowledge\src\main\java\springboot\config\CorsGlobalConfig.java) (推荐):
     ```java
     @Configuration
     public class CorsGlobalConfig implements WebMvcConfigurer {
         @Override
         public void addCorsMappings(CorsRegistry registry) {
             registry.addMapping("/**")              //允许访问的路径
                     //允许的域名,如果需要声明特定的端口号则加上字样如:80,考虑到浏览器的问题不建议特意声明80号端口
                     .allowedOrigins("http://localhost")
                     .allowCredentials(true)                     //允许携带认证信息cookie
                     .maxAge(3600)                               //重新预检验跨域的缓存时间,单位为秒
                     .allowedHeaders("*")                        //允许的请求头
                     .allowedMethods("GET", "POST", "PUT", "DELETE");//允许的请求方法
         }
     }
     ```

   - 创建跨域过滤器 [CorsFilterConfig.java](material\springboot-back-knowledge\src\main\java\springboot\config\CorsFilterConfig.java) (不推荐,会覆盖其他跨域请求的配置):
     ```java
     @Configuration
     public class CorsFilterConfig {
         @Bean
         public CorsFilter corsFilter() {
             UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
             CorsConfiguration config = new CorsConfiguration();
             config.setAllowCredentials(true);                       //允许携带认证信息cookie
             config.addAllowedOrigin("http://localhost");//允许的域名,如果需要声明特定的端口号则加上如 :80 字样
             config.addAllowedHeader("*");                           //允许的请求头
             config.addAllowedMethod("*");                           //允许的请求方法
             source.registerCorsConfiguration("/**", config); //允许访问的路径
             return new CorsFilter(source);
         }
     }
     ```

3. 在 controller 层创建方法 [ConnectionTestController.java](material\springboot-back-knowledge\src\main\java\springboot\controller\ConnectionTestController.java) ,访问 hello 网址:
   ```java
   @GetMapping("/hello")
   @CrossOrigin("http://localhost")//默认html请求的端口号就是80,但有些浏览不会在请求时带上80端口号所以不写
   public String helloPage(@RequestParam(value = "parameter", required = false) String param) {
       if (param != null) System.out.println(param);
       return " 后端 返回纯字符串数据:你好vue!";
   }
   ```

4. 对于前端 [AxiosSpringBootTest.vue](material\vue-front-knowledge\src\components\AxiosSpringBootTest.vue) :创建一个按钮,为其绑定 click 事件 getRequest() ,该事件使用 get 方法传递对应的参数:
   ```vue
   <script setup>
   import { doGet } from "../utils/request";
   const getRequest = () => {
     doGet(
       'http://localhost:8080/hello', //后端SpringBoot服务器要地址
       { parameter: ' 前端 返回json数据,内含字符串:你好Springboot!' } //这些数据会被写在网址后面
     ).then(resp => {
       alert(resp.data); //后端返回一个纯字符串
     })
   }
   </script>
   <template>
     <el-button type="primary" @click="getRequest">点此发送get请求与本地SpringBoot服务端进行连接测试</el-button>
   </template>
   ```

   同时启动后端服务器和前端网页,点击该按钮,观察弹出的对话框中的数据和在后端控制台中输出的数据

### 模拟前后端登陆信息交互

1. 对于后端:创建用来接收并封装前端传递而来的,post方法请求体中数据的类 [LoginMessageFromVue.java](material\springboot-back-knowledge\src\main\java\springboot\message\LoginMessageFromVue.java) :
   ```java
   @Data
   public class LoginMessageFromVue {
       private String userAccount;
       private String userPassword;
       private Boolean rememberMe;
   }
   ```

   该类的属性名称要与前端定义的请求体对象中的属性名称相同,否则要在字段中添加 @JsonProperty("映射的属性名") 注解声明其对应映射的json属性名

2. 创建用来传递信息给前端的类 [LoginMessageToVue.java](material\springboot-back-knowledge\src\main\java\springboot\message\LoginMessageToVue.java) ,该类的数据会被封装为 json 字符串写入响应体中:
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class LoginMessageToVue {
       private String title;
       private String message;
   }
   ```

3. 在controller层中 [ConnectionTestController.java](material\springboot-back-knowledge\src\main\java\springboot\controller\ConnectionTestController.java) 里创建对应的处理登陆信息的网址映射及方法:
   ```java
   @PostMapping("/login_demo")
   public LoginMessageToVue getUserFromVue(@RequestBody LoginMessageFromVue user) {
       if (user != null) System.out.println("接收到的账号密码类为: " + user);
       return new LoginMessageToVue("坤坤", "鸡你美不美");
   }
   ```

4. 对于前端 [AxiosSpringBootTest.vue](material\vue-front-knowledge\src\components\AxiosSpringBootTest.vue) ,根据以往的知识创建表单,为表单按钮绑定 submitMethod() 方法,测试数据的双向传递和处理:
   ```vue
   <script setup>
   import { doPost } from "../utils/request";
   import { ref } from "vue";
   let user = ref({ //绑定的账号密码与记住我
     userAccount: '',
     userPassword: '',
     rememberMe: false
   })
   const submitMethod = () => { //提交方法
     //then后的回调函数表示当请求成功后要执行的函数,通常为路由到某个页面
     doPost('http://localhost:8080/login_demo', user.value).then(resp => {
       alert('后端负责人 ' + resp.data.title + ' ,发来消息: ' + resp.data.message)
     }).catch(error => { //catch后的回调函数表示当执行失败后要执行的函数,如果连接服务器失败则不执行
       //可能的状态码:401身份未验证,402身份验证未通过,404页面不存在等,可以根据状态码使用不同方法处理不同的异常
       alert('登录失败,状态码: ' + error.response.status) 
       console.log(error.response.data)
     }).finally(()=>{ //finally后的回调函数表示无论成败都要执行的函数,甚至连接服务器失败也可能执行
       console.log('登陆方法执行完毕')
     })
   }
   </script>
   <template>
     <el-form label-width="60px" style=""><!-- 登陆表单 -->
       <el-form-item label="账号">
         <el-input v-model="user.userAccount" />
       </el-form-item>
       <el-form-item label="密码">
         <el-input type="password" v-model="user.userPassword" />
       </el-form-item>
       <el-form-item>
         <el-button type="primary" @click="submitMethod">点击这里将账号密码信息发送给后端</el-button>
       </el-form-item>
       <el-form-item>
         <el-checkbox label="记住我" size="large" v-model="user.rememberMe" />
       </el-form-item>
     </el-form>
   </template>
   ```

5. 运行前端和后端,随意输入账号和密码,点击提交按钮,观察弹出的对话框中的内容,并观察后端控制台的输出

## [axios拦截器](https://axios-http.com/zh/docs/interceptors),token的获取与验证

1. 对于后端:创建一个拦截器 [TokenInterceptor.java](material\springboot-back-knowledge\src\main\java\springboot\interceptor\TokenInterceptor.java) 来确定拦截规则,从请求头中获取token并模拟token验证过程:
   ```java
   public class TokenInterceptor implements HandlerInterceptor {
       @Override
       public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           // 注意:浏览器进行ajax跨域请求前会发送一个OPTIONS类型的请求获取服务器支持的HTTP请求方式,该请求不能被拦截否则报错
           if (request.getMethod().equals("OPTIONS")) return true;
           String token = request.getHeader("Authorization"); //从请求头中获取 token
           if (token == null) { //如果没有token
               response.sendError(401, "用户身份未验证"); //401 未授权
               return false; //拦截
           } else if (!token.equals("rnfmabj")) { //如果token不正确
               response.sendError(402, "用户身份验证未通过");
               return false;
           }
           return true; //这里省略了验签,从token中获取用户名并从redis查询对应token的过程
       }
   }
   ```

   在配置类 [TokenInterceptor.java](material\springboot-back-knowledge\src\main\java\springboot\interceptor\TokenInterceptor.java) 中注册该拦截器:
   ```java
   @Configuration
   public class TokenInterceptorConfig implements WebMvcConfigurer {
       @Override
       public void addInterceptors(InterceptorRegistry registry) {
           String[] addPathPatterns = {"/token/**"}; //拦截路径
           //放行路径,这两个路径用于获取token
           String[] excludePathPatterns = {"/token/get_token", "/token/get_error_token"}; 
           //TokenInterceptor为自定义类
           registry.addInterceptor(new TokenInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
       }
   }
   ```

   创建对应的网页控制器 [TokenTestController.java](material\springboot-back-knowledge\src\main\java\springboot\controller\TokenTestController.java) ,模拟token的验证获取过程:
   ```java
   @RestController
   @RequestMapping("/token")
   public class TokenTestController {
       @GetMapping("/blocked_page") //该页面只有在被拦截器检验通过后访问到,需要用户拥有正确的token
       public LoginMessageToVue blockedPage() {
           return new LoginMessageToVue("皮皮龙", "你发现了我的秘密哈哈哈哈");
       }
       @GetMapping("/get_token") //访问该页面将会授予前端一个正确的token
       public LoginMessageToVue getToken() {
           return new LoginMessageToVue("token", "rnfmabj");
       }
       @GetMapping("/get_error_token") //访问该页面会授予前端一个错误的token,携带该token的请求将无法通过拦截器的验证
       public LoginMessageToVue getErrorToken() {
           return new LoginMessageToVue("token", "Angela");
       }
   }
   ```

2. 对于前端,创建一个 [token-request.js](material\vue-front-knowledge\src\utils\token-request.js) 模块,导出其中的 get 请求方法 tokenRequest():
   ```javascript
   import axios from "axios";
   export const TOKEN_NAME = 'Authorization'// 要写入请求头的属性名(token)
   axios.defaults.baseURL = 'http://localhost:8080/token';//写入具体的url,使用本页面的方法填写url时可以省略一部分
   axios.interceptors.request.use((config) => { //请求拦截器,在发送请求前做些什么,用本页面任意导出的方法时生效
       let token = window.sessionStorage.getItem(TOKEN_NAME)//会话存储,用户关闭浏览器窗口后会消失,存储时间短
       if (!token) { //如果在会话存储中未找到token
           token = window.localStorage.getItem(TOKEN_NAME)//本地存储,浏览器关闭后仍然保留,存储时间长
       }
       if (token) { //如果token不为空
           config.headers[TOKEN_NAME] = token //将token写入请求头
           // config.headers.Authorization = token //另一种写法
       }
       return config;
   }, (error) => {
       return Promise.reject(error); //对请求错误做些什么,固定写法不要动
   });
   //响应拦截器,2xx 范围内的状态码都会触发该函数,response为响应数据,对使用本页面任意导出的方法后返回的响应体都生效
   axios.interceptors.response.use((response) => {
       return response;
   }, (error) => { //超出 2xx 范围的状态码都会触发该函数,对响应错误做点什么
       //输出错误状态码和从后端返回的错误消息,只有错误状态码为401或402时后端才会返回消息(即message不为空)
       alert('错误状态码: ' + error.response.status + ' 错误消息: ' + error.response.data.message)
       return Promise.reject(error); //同理,固定写法
   });
   export const tokenRequest = (url) => { //要导出的操作方法
       return axios({
           method: 'get',
           url: url,
       })
   }
   ```

   在 [AxiosSpringBootTest.vue](material\vue-front-knowledge\src\components\AxiosSpringBootTest.vue) 中,写入几个按钮,这些按钮的作用详见其内容体:
   ```vue
   <script setup>
   import { tokenRequest, TOKEN_NAME } from "../utils/token-request";
   const visitBlockedPage = () => { //访问需要身份验证的网址,具有 token 才能访问,没有的话返回401错误
     tokenRequest('/blocked_page').then(resp => {
       alert('后端负责人 ' + resp.data.title + ' ,发来消息: ' + resp.data.message)
     })
   }
   const deleteToken = () => { //删除本地存储与会话存储中的 token
     window.localStorage.removeItem(TOKEN_NAME);
     window.sessionStorage.removeItem(TOKEN_NAME);
   }
   const getToken = (url) => { //当获取到新的token时,删除之前存储的token并将其存入会话中
     tokenRequest(url).then(resp => {
       deleteToken()
       let token = resp.data.message
    // window.localStorage.setItem(TOKEN_NAME, token); //长存储
       window.sessionStorage.setItem(TOKEN_NAME, token); //短存储
       alert('成功获取用户身份令牌token: ' + token)
     })
   }
   </script>
   <template> 
     <el-button type="primary" @click="visitBlockedPage">点此访问需要token验证才能通过的页面</el-button><hr />
     <el-button type="primary" @click="deleteToken">点此删除用户身份token令牌</el-button><hr />
     <el-button type="primary" @click="getToken('/get_token')">点此获取用户身份token令牌</el-button><hr />
     <el-button type="primary" @click="getToken('/get_error_token')">点此获取伪造的用户身份token令牌</el-button>
   </template>
   ```
   
3. 同时运行前端和后端,在浏览器的审查元素中应用一栏里点击"会话存储空间",点击第一个按钮观察报错弹窗,再在点击第三,四个按钮获取不同的token后分别点击第一个按钮,观察弹窗内容同时观察"会话存储空间"里的内容变化,最后点击第二个按钮观察"会话存储空间"里的内容变化

