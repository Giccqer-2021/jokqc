# aop切面,validation规范,异常处理

*本说明使用工程 [idea-springboot知识(默认)](material\idea-springboot-knowledge.bat)* 

## aop切面

1. aop切面编程方法一般用于调用日志的输出,或是使得自定义注解产生某些效果.使用aop需要先导入依赖:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-aop</artifactId>
   </dependency>
   ```

2. 使用方法描述来监视某些方法的执行:创建配置类  [AOPMonitorConfig.java](material\springboot-knowledge\src\main\java\springboot\config\AOPMonitorConfig.java) ,创建切点表达式相关的字符串,并配置如何监视表达式中所描述的方法:
   ```java
   @Aspect
   @Configuration
   public class AOPMonitorConfig {
        //切点表达式字符串,表示监视 controller 包下 DefaultController 类中所有 public 方法
       private static final String POINTCUT_EXPRESSION = "execution(public * springboot.controller.DefaultController.*(..))";
       //执行 POINTCUT_EXPRESSION 所描述的方法时会监视该方法并执行某些方法,使用 @Around() 注解环绕监视该方法的执行过程
       @Around(POINTCUT_EXPRESSION)
       @Order(1) //若存在多个同类型的aop方法,该注解决定了这些方法的执行顺序,值越小优先级越高
       public Object aroundByExpression(ProceedingJoinPoint joinPoint) throws Throwable {
           System.out.println("======AOP监视环绕前(使用 express描述 )======");
           Object result = joinPoint.proceed(); // 允许被监视的方法执行, result 为被监视的方法的返回值
           System.out.println("======AOP监视环绕后(使用 express描述 )======");
           return result; //要写上,声明该方法能正常返回结果
       }
   }
   ```

   该方法监视了第一次创建工程时控制层 [DefaultController.java](material\springboot-knowledge\src\main\java\springboot\controller\DefaultController.java) 中 /hello 页面输出方法,在该方法中插入输出到控制台的语句:
   
   ```java
   System.out.println("------已输出Hello页面------");
   ```
   
   运行springboot并访问 http://localhost:8081/springboot-knowledge/hello 页面,观察控制台输出,结果如下:
   
   > ======AOP监视环绕前(使用 express描述 )======
   > ------已输出Hello页面------
   > ======AOP监视环绕后(使用 express描述 )======
   
3. 自定义注解,使该注解"起到实际作用",用该注解来监控某些方法的执行:创建包 annotation ,创建自定义注解 [CustomAnnotation.java](material\springboot-knowledge\src\main\java\springboot\annotation\CustomAnnotation.java) :
   ```java
   @Target(ElementType.METHOD) // 注解作用在方法上
   @Retention(RetentionPolicy.RUNTIME) // 注解保留在运行时
   @Documented  // 所有使用该注解的地方,在生成javadoc文档时保留该注解的描述
   public @interface CustomAnnotation {
       String value() default "";
   }
   ```
   
   为该注解声明aop编程,在 [AOPMonitorConfig.java](material\springboot-knowledge\src\main\java\springboot\config\AOPMonitorConfig.java) 中添加方法:
   ```java
   // @annotation() 括号中的字符串要与本方法CustomAnnotation注解形参的名字一致
   @Around("@annotation(customAnnotation)") 
   public Object aroundByAnnotation(ProceedingJoinPoint joinPoint, CustomAnnotation customAnnotation) throws Throwable {
       System.out.println("======AOP监视环绕前(使用 annotation注解 )======");
       System.out.println("自定义注解的value值: " + customAnnotation.value()); //获取自定义注解的value值
       Object result = joinPoint.proceed();
       System.out.println("======AOP监视环绕后(使用 annotation注解 )======");
       return result;
   }
   ```
   
   在  [DefaultController.java](material\springboot-knowledge\src\main\java\springboot\controller\DefaultController.java) 中的 /hello 页面方法上添加该自定义注解:
   ```java
   @CustomAnnotation(">>>这是一个自定义的注解<<<")
   ```
   
   运行springboot服务器,打开 http://localhost:8081/springboot-knowledge/hello 控制台输出结果如下:
   
   > ======AOP监视环绕前(使用 annotation注解 )======
   > 自定义注解的value值: >>>这是一个自定义的注解<<<
   > ======AOP监视环绕前(使用 express描述 )======
   > ------已输出Hello页面------
   > ======AOP监视环绕后(使用 express描述 )======
   > ======AOP监视环绕后(使用 annotation注解 )======
   
4. 在 aop 的配置类中,可使用的配置注解如下:

   |       注解        |             属性              |                             用途                             | 用于 |
   | :---------------: | :---------------------------: | :----------------------------------------------------------: | :--: |
   |    @Pointcut()    |        execution表达式        | 定义切入点,通常使用一个返回值和方法体内部<br />皆为空的方法上,仅用作标记声明而不执行任何方法 | 方法 |
   |     @Before()     | execution表达式或切入点方法名 |                     目标方法执行之前执行                     | 方法 |
   |     @After()      | execution表达式或切入点方法名 |            目标方法执行之后必定执行，无论是否报错            | 方法 |
   | @AfterReturning() | execution表达式或切入点方法名 |               目标方法有返回值且正常返回后执行               | 方法 |
   | @AfterThrowing()  | execution表达式或切入点方法名 |                    目标方法抛出异常后执行                    | 方法 |
   |     @Around()     | execution表达式或切入点方法名 |      环绕目标方法执行,可以获取到目标方法的入参和返回值       | 方法 |
   
5. 关于 execution表达式 :本质上是一个字符串,若直接使用 execution(方法描述) 描述某方法则使用以下 方法描述 规则:

   > 以 execution(public * springboot.controller.DefaultController.*(..)) 为例
   > public 表示方法权限标识符,可以省略
   > 后面的 * 表示返回值的类型任意
   > 后面的 springboot.controller 表示包名
   > 后面的 DefaultController 表示 DefaultController 类,可以用 .* (表示本包内所有类) 或 ..* (本包内的包内的类也包含在内)代替
   > 后面的 .* 表示任何方法名
   > 后面的 (..) 表示参数类型,两个点 .. 表示任意数量,任何参数类型

   如果描述某注解则使用 @annotation(注解名) 描述方法注解或 @within(注解名) 描述类注解

## dto,bean validation注解,异常处理

### 关于dto(数据传输对象)

 dto (Data Transfer Object) 是一种用于在不同层或组件之间传输数据的对象,主要用于封装和传递数据,以实现数据的跨层或跨组件的传递.一般只包含数据字段(属性)和对应的getter和setter方法,而不包含业务逻辑
与 dao (Data Access Object) 不同,dao用于封装对数据持久层,执行对数据的增删改查操作,可以包含数据库连接,查询语句的执行,事务管理等与数据访问相关的逻辑

### 使用 validation 解决非空报错问题

1. 使用 bean validation 注解前需要导入相关依赖:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```

2. 如果某个类所在的包声明了 @NonNullApi 包注解,而你又重写了该类,如果你不以同样的方法声明包注解则在重写类中方法时idea编辑器会弹出警告,如 interceptor 包下的 [LoginInterceptor.java](material\springboot-knowledge\src\main\java\springboot\interceptor\LoginInterceptor.java)  类.若阻止idea警告,则需要自行为该包标记包注解.在该类的包下创建 [package-info.java](material\springboot-knowledge\src\main\java\springboot\interceptor\package-info.java) 文件,并添加 @NonNullApi 注解:
   ```java
   @NonNullApi //该包下所有类中的方法,其传入的形参和返回值都不能为null
   package springboot.interceptor; //所有java包注解都标记在上面
   import org.springframework.lang.NonNullApi;
   ```

### 使用  validation 注解校验字段

1. bean validation 为专门用来校验 controller 层中从前端返回数据合法性的校验注解.创建包 dto ,创建数据传输类 [ValidationDto.java](material\springboot-knowledge\src\main\java\springboot\dto\ValidationDto.java) ,为其中的每一个字段添加校验规则:
   ```java
   @Data
   public class ValidationDto {
       @Positive(message = "id必须大于0") // @Positive 用于数值型数据,该数据必须为正整数
       @NotNull(message = "id不能为空") // @NotNull 对象不可为空
       private Integer id;
       @Size(min = 2, max = 10, message = "name长度必须在2-10之间") // @Size 用于字符串数据,该数据长度必须在min-max之间
       @NotBlank(message = "name不能为空") // @NotBlank 用于字符串数据,数据非空且去除两边空格后不可以是空串
       private String name;
       // @Pattern 用于字符串数据,该数据必须匹配java正则表达式,该表达式声明密码必须是大小写字母或是数字,长度为6-12位
       @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "密码格式不正确") 
       @NotNull(message = "密码不能为空")
       private String password;
       @NotNull(message = "性别不能为空")
       private Boolean isMale;
       @Max(value = 150, message = "年龄不能超过150") // @Max 用于数值型数据,该数据必须小于等于max
       @Min(value = 0, message = "年龄不能小于0") // @Min 用于数值型数据,该数据必须大于等于min
       @NotNull(message = "年龄不能为空")
       private Integer age;
       @Max(value = 99999999999L, message = "手机号长度必须为11位")
       @Min(value = 10000000000L, message = "手机号长度必须为11位")
       private Long phone;
       @Email(message = "邮箱格式不正确") // @Email 用于字符串数据,该数据必须为邮箱格式
       private String email;
   }
   ```

2. 在 controller 层中创建类  [ValidationController.java](material\springboot-knowledge\src\main\java\springboot\controller\ValidationController.java) ,添加一个使用该dto的网页控制方法,其中的形参字段要添加 @Validated 注解
   ```java
   @RestController
   @RequestMapping("/validation")
   public class ValidationController {
       @GetMapping(value = "/register")
       public ValidationDto getValidationPage(@Validated ValidationDto validationDto) {
           System.out.println("Get方法收到来自前端的数据: " + validationDto);
           return validationDto;
       }
   }
   ```

   其中 @Validated 注解可以在 类,方法和形参 上使用,属于Spring Validation框架注解
   @Valid 注解的使用方法与之相似,该注解可添加于方法,构造方法,形参和字段上,属于Java EE提供的标准注解
   
3. 启动后端,访问以下验证参数完全合法的网址(这里建议使用postman软件进行操作)

   > [正确网址](http://localhost:8081/springboot-knowledge/validation/register?id=100&name=殷红迷雾&password=RedFog&isMale=false&age=39&phone=13812345678&email=114514@library.com)

   再测试验证参数不完全合法的网址

   > [错误网址](http://localhost:8081/springboot-knowledge/validation/register?id=-1&name=殷红迷雾&password=RedFog&isMale=false&phone=13812345678111222&email=114514@library.com@@@)

   观察网页和控制台输出(包括报错的信息)
   
4. 以下为默认的validation**字段**注解,对于空与非空的检查:

   |   注解    |                    用途                     | 应用的字段类型  |
   | :-------: | :-----------------------------------------: | :-------------: |
   | @NotBlank |    该字符串不为空,且两边去空格后不为空串    |     字符串      |
   | @NotEmpty | 该集合或字符串不为空且集合或字符串长度不为0 | 集合对象,字符串 |
   | @NotNull  |               该字段不能为空                |      任意       |
   |   @Null   |               该字段必须为空                |      任意       |

   除此之外,其他字段注解所标记的字段的值为 null 时都会被验证通过,这些字段有:

   |             注解             |                用途                |      应用的字段类型       |
   | :--------------------------: | :--------------------------------: | :-----------------------: |
   | @DecimalMax(某值),@Max(某值) |        其值必须小于等于某值        |           数值            |
   | @DecimalMin(某值),@Min(某值) |        其值必须大于等于某值        |           数值            |
   | @Digits(整数精度, 小数精度)  | 验证字符串是否为符合指定格式的数值 |          字符串           |
   |          @Positive           |           其值必须为正数           |           数值            |
   |       @PositiveOrZero        |          其值必须为非负数          |           数值            |
   |          @Negative           |           其值必须为负数           |           数值            |
   |       @NegativeOrZero        |          其值必须为非正数          |           数值            |
   |         @AssertFalse         |            必须为 false            |          布尔值           |
   |         @AssertTrue          |            必须为 true             |          布尔值           |
   |    @Size(最大值, 最小值)     |   该对象的size大小是否在某区间内   | 字符串、数组、集合、Map等 |
   |           @Future            |        必须是一个将来的日期        |           日期            |
   |       @FutureOrPresent       |        是否是将来或现在日期        |           日期            |
   |            @Past             |        必须是一个过去的日期        |           日期            |
   |        @PastOrPresent        |        是否是过去或现在日期        |           日期            |
   |            @Email            |     该字符串必须是电子邮箱地址     |          字符串           |
   |     @Pattern(正则表达式)     |  该字符串必须符合指定的正则表达式  |          字符串           |

   关于 @Pattern() 注解的正则表达式看 [这里](https://blog.csdn.net/linmengmeng_1314/article/details/103176378) 

### 自定义全局异常处理

1. 在 controller 层中创建异常处理控制层类  [ExceptionController.java](material\springboot-knowledge\src\main\java\springboot\controller\ExceptionController.java) ,使用 @ControllerAdvice 类注解:
   ```java
   @ControllerAdvice
   public class ExceptionController {
       // 添加 @ExceptionHandler("要处理的异常类. class") 来声明当发生该异常时对视图进行处理的方法
       // 本方法形参中可指定HttpServletRequest,HttpServletResponse,这里形参指定对应异常类对象,获取该类中相关的错误信息
       @ExceptionHandler(MethodArgumentNotValidException.class) //MethodArgumentNotValidException:参数校验异常
       @ResponseBody
       public ResponseEntity<MessageDto> exception(MethodArgumentNotValidException exception) {
           //response.sendError(400); //从本质上来说,以下代码就相当于这一句话
           List<ObjectError> errors = exception.getAllErrors(); //获取全部错误信息的List集合
           StringBuilder errorStrings = new StringBuilder();
           for (ObjectError error : errors) { //遍历
               //使用 getDefaultMessage() 方法能获取到这些字段上的注解中的自定义 message 错误文本消息
               errorStrings.append(error.getDefaultMessage()).append(" "); //把它们连起来,用空格分隔
           }
           // MessageDto 为自定义dto类,用来封装返回给前端的消息,若直接返回该类的对象则会被前端认为是"请求成功"(200),
           // 要声明返回错误消息,使用 ResponseEntity.badRequest() ,返回的错误代码为400
           return ResponseEntity.badRequest().body(new MessageDto(errorStrings.toString()));
       }
   }
   ```

   @ControllerAdvice 注解的使用方式与 @Controller 注解大同小异,二者皆为组件但并非继承关系,与之不同的是,@ControllerAdvice 用来处理发生异常时应当返回前端的数据,而不是根据特定的网址决定返回数据.
   同理, 也可以使用@RestControllerAdvice 来自定义全局异常处理,其与 @RestController 注解大同小异
   
   该方法使用的封装发送给前端消息的dto为  [MessageDto.java](material\springboot-knowledge\src\main\java\springboot\dto\MessageDto.java) :
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class MessageDto {
       private String message;
   }
   ```
   
2. 启动服务器,访问以下传递不合法参数的网址:

   > [错误网址](http://localhost:8081/springboot-knowledge/validation/register?id=-1&name=殷红迷雾&password=RedFog&isMale=false&phone=13812345678111222&email=114514@library.com@@@)

   观察后端控制台和前端浏览器页面的输出是否符合自定义的异常处理方法

### 自定义参数校验注解

1. 该注解的定义为  [AdminPasswordAnnotation.java](material\springboot-knowledge\src\main\java\springboot\annotation\AdminPasswordAnnotation.java) :
    ```java
    @Target(ElementType.FIELD) // 注解作用在字段上
    @Retention(RetentionPolicy.RUNTIME) // 注解保留在运行时
    @Documented  // 所有使用该注解的地方,在生成javadoc文档时保留该注解的描述
    @Constraint(validatedBy = AdminPasswordValidator.class) // 该注解需要一个自定义的实现类来完成校验
    public @interface AdminPasswordAnnotation {
        String value() default ""; //本次负责授权的管理员的名字
    	// message() 发生错误时, 返回的默认错误信息提示,该方法必须被添加,其名称和用法是固定的
        String message() default "管理权限密码输入错误"; 
        Class<?>[] groups() default {}; //该方法并未被使用,但根据规范,需要添加
        Class<? extends Payload>[] payload() default {}; //该方法并未被使用,但根据规范,需要添加
    }
    ```

   该注解在定义时需要添加  @Constraint(自定义的校验方法实现类. class) 注解,同时需要加入一些固定写法的抽象方法.该自定义的校验方法实现类为 validator 包下的 [AdminPasswordValidator.java](material\springboot-knowledge\src\main\java\springboot\validator\AdminPasswordValidator.java) :
   ```java
   public class AdminPasswordValidator implements ConstraintValidator<AdminPasswordAnnotation, String> {
       String admin;//自定义注解的value值,即管理员名字,在注解初始化时赋值.
       @Override //注解初始化时调用,将注解的value值赋值给admin全局字段.该方法有其默认的实现方法,可以不重写
       public void initialize(AdminPasswordAnnotation constraintAnnotation) {
           this.admin = constraintAnnotation.value();
       }
       @Override //自定义具体校验规则,这里假设猪猪侠,灰太狼,以实玛利是管理员,只有输入口令正确才会校验通过
       public boolean isValid(String value, ConstraintValidatorContext context) {
           // return为true时校验通过,false时校验失败,value为校验字段的值
           if (this.admin.equals("猪猪侠") && value.equals("超级棒棒糖")) return true;
           else if (this.admin.equals("灰太狼") && value.equals("我还会回来的")) return true;
           else return this.admin.equals("以实玛利") && value.equals("都是你的错");
       }
   }
   
   ```

   其中 AdminPasswordAnnotation 为刚刚自定义完成的注解.该类需实现 ConstraintValidator<自定义的校验注解,要校验的字段类型> 泛型接口,至少要重写 isValid() 方法

2. 创建拥有使用该注解字段的dto类  [AdminPasswordDto.java](material\springboot-knowledge\src\main\java\springboot\dto\AdminPasswordDto.java) ,这里假设管理员名字(注解value值) 是以实玛利,密码来自前端发来的数据:
   ```java
   @Data
   public class AdminPasswordDto {
       @AdminPasswordAnnotation("以实玛利")
       private String password;
   }
   ```

3. 在 controller 层中的 [ValidationController.java](material\springboot-knowledge\src\main\java\springboot\controller\ValidationController.java) 类里,添加接收前端密码的网址控制方法:
   ```java
   @GetMapping(value = "/admin")
   public String adminPage(@Validated AdminPasswordDto passwordDto) {
       System.out.println("前端发来的管理员密码: " + passwordDto);
       return "<p>管理员身份认证成功!</p>";
   }
   ```

4. 启动服务器,分别访问输入密码正确的网址和错误的网址,观察网页页面和后端控制台输出:

   > [口令正确的网址](http://localhost:8081/springboot-knowledge/validation/admin?password=都是你的错) 
   > [口令错误的网址](http://localhost:8081/springboot-knowledge/validation/admin?password=这不是你的错) 

