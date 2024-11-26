# 持久层,服务层,springboot-redis

## 持久层mapper

1. 在  [pom.xml](material\springboot-knowledge\pom.xml) 文件中导入 mybatis-spring-boot-starter , mysql-connector-j [依赖](https://mvnrepository.com/)
   ```xml
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
       <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>3.0.3</version>
   </dependency>
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-j</artifactId>
       <version>9.0.0</version>
   </dependency>
   ```
   
2. 创建数据表,并插入相关数据:
   ```sql
   CREATE TABLE `knowledge_database`.`company_table` (
     `employee_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
     `employee_name` VARCHAR(20) NOT NULL,
     `is_male` TINYINT(1) UNSIGNED NOT NULL,
     `employee_phone` BIGINT UNSIGNED NULL,
     `notes` VARCHAR(60) NULL,
     PRIMARY KEY (`employee_id`),
     UNIQUE INDEX `employee_id_UNIQUE` (`employee_id` ASC) VISIBLE)
   ENGINE = InnoDB
   DEFAULT CHARACTER SET = utf8
   COMMENT = 'The demo company_table table';
   
   INSERT INTO `knowledge_database`.`company_table` (`employee_id`, `employee_name`, `is_male`, `employee_phone`, `notes`) VALUES ('1', '王晓晓', '0', '12323234542', '该员工积极上进,值得表扬');
   INSERT INTO `knowledge_database`.`company_table` (`employee_name`, `is_male`, `employee_phone`, `notes`) VALUES ('王洛', '1', '23123221245', '该员工喜欢偷懒');
   INSERT INTO `knowledge_database`.`company_table` (`employee_name`, `is_male`, `employee_phone`) VALUES ('鹏鹏', '1', '12313423213');
   INSERT INTO `knowledge_database`.`company_table` (`employee_name`, `is_male`) VALUES ('王大美', '0');
   ```

3. 在 [application.yml](material\springboot-knowledge\src\main\resources\application.yml) 文件中添加(网址,用户名和密码要根据实际情况配置):

   ```yml
   spring:
     datasource:
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/knowledge_database
       username: root
       password: 123456@abcdef
   ```

4. 创建包 entity , mapper ,在 entity 中创建类  [CompanyTable.java](material\springboot-knowledge\src\main\java\springboot\entity\CompanyTable.java) :

   ```java
   @Data
   public class CompanyTable {
       private Integer employeeId;
       private String employeeName;
       private Boolean isMale;
       private Long employeePhone;
       private String notes;
   }
   ```
   
   然后在 mapper 中创建接口  [CompanyTableMapper.java](material\springboot-knowledge\src\main\java\springboot\mapper\CompanyTableMapper.java)  :
   ```java
   @Mapper
   @Repository
   public interface CompanyTableMapper {
       List<CompanyTable> getAllEmployees();//获取全部员工信息,用CompanyTable类的对象数组进行封装
   }
   ```
   
5. 在resources下创建文件夹 mapper ,创建  [CompanyTableMapper.xml](material\springboot-knowledge\src\main\resources\mapper\CompanyTableMapper.xml)   ,利用 [这个文件](material\Mapper.xml) ,写入:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="springboot.mapper.CompanyTableMapper">
       <select id="getAllEmployees" resultType="springboot.entity.CompanyTable">
           select * from company_table
       </select>
   </mapper>
   ```

   然后在  [application.yml](material\springboot-knowledge\src\main\resources\application.yml) 配置文件中加入指明其映射文件:
   ```yml
   mybatis:
     mapper-locations:
       - classpath:mapper/*.xml
     configuration:
       map-underscore-to-camel-case: true #将数据表中用下划线命名的字段转换为驼峰式命名
       log-impl: org.apache.ibatis.logging.stdout.StdOutImpl #输出sql语句到控制台
   ```
   
6. 创建测试类  [MybatisTest.java](material\springboot-knowledge\src\test\java\springboot\MybatisTest.java)  ,写入:
   ```java
   @SpringBootTest
   public class MybatisTest {
       @Autowired
       private CompanyTableMapper companyTableMapper;//mapper实现类
       @Test
       public void printTest() {
           companyTableMapper.getAllEmployees().forEach(System.out::println);
       }
   }
   ```
   
   运行测试,检查是否能正常输出信息列表,观察从控制台输出的sql语句
   
7. 持久层注解:

|    注解     | 属性 |                             用途                             | 用于 |
| :---------: | :--: | :----------------------------------------------------------: | :--: |
|   @Mapper   |      | 将一个类声明为持久层组件,也可以在启动类标记<br />@MapperScan('包路径')注解来代替该注解 |  类  |
| @Repository |      | 与上方注解作用相同,该注解是由Springboot本身<br />提供的,而@Mapper由mybatis提供,标记时可以二者<br />一起使用防止idea报错 |  类  |

## 服务层service

1. 创建包 service ,创建接口  [CompanyService.java](material\springboot-knowledge\src\main\java\springboot\service\CompanyService.java) ,写入:
   ```java
   public interface CompanyService {
       List<CompanyTable> getAllEmployeesData();
   }
   ```

2. 创建包 service.impl,创建类  [CompanyServiceImpl.java](material\springboot-knowledge\src\main\java\springboot\service\impl\CompanyServiceImpl.java) 继承该接口,写入:
   ```java
   @Service
   public class CompanyServiceImpl implements CompanyService {
       @Autowired
       private CompanyTableMapper companyTableMapper;//mapper实现类
       @Override
       public List<CompanyTable> getAllEmployeesData() {
           return companyTableMapper.getAllEmployees();//获取全部的员工信息
       }
   }
   ```

3. 在 controller 包中创建 [DatabaseController.java](material\springboot-knowledge\src\main\java\springboot\controller\DatabaseController.java) ,写入:
   ```java
   @RestController
   @RequestMapping("/database")
   public class DatabaseController {
       @Autowired
       private CompanyService companyService;//service实现类
       @GetMapping("/get-all-employees")
       public List<CompanyTable> getAllEmployeesData() {
           return companyService.getAllEmployeesData();//返回被json格式化后的结果
       }
   }
   ```
   
   访问页面 http://localhost:8081/springboot-knowledge/database/get-all-employees 检查数据是否能正常传输,,并观察从控制台输出的sql语句
   
4. 服务层注解:

|      注解      | 属性 |                             用途                             | 用于 |
| :------------: | :--: | :----------------------------------------------------------: | :--: |
|    @Service    |      |                   将一个类声明为服务层组件                   |  类  |
| @Transactional |      | 声明该方法启用事务管理,可以实现回滚等功能,启动类<br />必须添加 @EnableTransactionManagement 注解才能生效 | 方法 |

## springboot-redis

### 配置,架构

1. [pom.xml](material\springboot-knowledge\pom.xml) 导入依赖:

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

2. 在  [application.yml](material\springboot-knowledge\src\main\resources\application.yml) 中添加配置(ip端口号根据实际情况设置):
   ```yml
   spring:
     data:
       redis: 
         host: 127.0.0.1
         port: 6379
         database: 0 #配置需要使用哪个数据库
         password: 123456@abcdef #redis密码,如果未设置则不写
   ```

3. 在mapper层  [CompanyTableMapper.java](material\springboot-knowledge\src\main\java\springboot\mapper\CompanyTableMapper.java)  接口中添加方法:

   ```java
   List<CompanyTable> getEmployeeById(int id);
   ```

   同时,在  [CompanyTableMapper.xml](material\springboot-knowledge\src\main\resources\mapper\CompanyTableMapper.xml) 文件中写入对应sql语句:
   ```xml
   <select id="getEmployeeById" resultType="springboot.entity.CompanyTable">
       select * from company_table where employee_id = #{employeeId, jdbcType=INTEGER}
   </select>
   ```

4. 在service层中, [CompanyService.java](material\springboot-knowledge\src\main\java\springboot\service\CompanyService.java) 接口添加方法:
   ```java
   CompanyTable getTheFirstEmployee();//获取di=1的员工信息
   ```

   实现类  [CompanyServiceImpl.java](material\springboot-knowledge\src\main\java\springboot\service\impl\CompanyServiceImpl.java) 中实现方法,获得第一号员工的数据:
   ```java
   @Override
   public CompanyTable getTheFirstEmployee() {//获取第一位员工的信息
       List<CompanyTable> result = companyTableMapper.getEmployeeById(1);
       if (!result.isEmpty()) return result.getFirst();
       return null;
   }
   ```

5. 创建配置类 [RedisConfig.java](material\springboot-knowledge\src\main\java\springboot\config\RedisConfig.java) ,将redis默认的java对象序列化方式改为json对象序列化方式
   ```java
   @Configuration
   public class RedisConfig {
   //使用外部注入的 RedisConnectionFactory 类型的对象注入该方法的形参,该对象会根据 application.yml 文件中的参数进行默认配置,在此基础上只需要修改部分内容即可.
       @Bean("defaultRedisTemplate") //注解声明了该返回值对象在 springIOC 容器中的名称,引用该对象时要写入该名称
       public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
           RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
           redisTemplate.setConnectionFactory(redisConnectionFactory);
           redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());//设置键值序列化方式
           //设置值json序列化方式,使用 Jackson2JsonRedisSerializer 代替默认的java序列化方式
           //若要使用字符串序列化方式则使用 StringRedisSerializer
           redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
           redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());//key hashmap序列化
           redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());//value hashmap序列化
           redisTemplate.afterPropertiesSet();//在bean属性设置完成之后执行一些特定的操作
           return redisTemplate;
       }
   }
   ```

6. 在controller层 [DatabaseController.java](material\springboot-knowledge\src\main\java\springboot\controller\DatabaseController.java) 类中,注入redis控制对象,并编写相关的网页控制方法:
   ```java
   @Autowired
   @Qualifier("defaultRedisTemplate") //redis模板对象，引用redis数据库所必要的对象,已在redis配置类中创建完毕.
   private RedisTemplate<String, Object> redisTemplate;
   //先查看redis服务器缓存中是否存在我们需要的信息,若为否,从mysql中查询并返回结果,在此过程中将数据写入缓存
   @GetMapping("/get-the-first-employee")
   public Object getTheFirstEmployee() {
       Object theFirstEmployee = redisTemplate.opsForValue().get("theFirstEmployee");//从redis缓存中获取对象信息
       if (theFirstEmployee == null) {//如果结果为空
           System.out.println("正在执行MySQL查询");
           //以下语句为从mysql查询数据的过程，这个过程不宜让大量的请求同时进行
           theFirstEmployee = companyService.getTheFirstEmployee();
           redisTemplate.opsForValue().set("theFirstEmployee", theFirstEmployee);//将查询结果写入redis缓存
           return theFirstEmployee;//返回结果，controller会自动将其转化为json类型字符串
       } else {
           System.out.println("正在执行Redis查询");
       }
       return theFirstEmployee;//从redis查询到的结果一定是json类型字符串
   }
   ```
   
7. 启动服务器,访问 http://localhost:8081/springboot-knowledge/database/get-the-first-employee 网址两次,观察控制台输出

### 自定义数据库配置

自定义数据库配置旨在不使用默认配置的情况下启用不同编号的数据库,需使用新的连接工厂对象进行配置,在 [RedisConfig.java](material\springboot-knowledge\src\main\java\springboot\config\RedisConfig.java) 配置类中插入以下配置:
```java
@Bean("redisTemplateDb1")
public StringRedisTemplate redisTemplateDb1() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);//新工厂对象
    configuration.setDatabase(1); // 设置要使用的数据库
    configuration.setPassword("123456@abcdef"); // 设置密码
    // 配置连接客户端对象,该对象由于没有自定义的任何配置可以省略
    LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder().build();
    // 配置连接工厂对象
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
    connectionFactory.start(); // 不写就报错
    return new StringRedisTemplate(connectionFactory); //StringRedisTemplate 是 RedisTemplate 的子类
}
```

本次直接使用已配置好字符串序列化方式的类 StringRedisTemplate 的对象作为返回值,在此基础上进行自定义配置

### 不同redis数据类型的操作

1. 为演示增删改查操作方法,本次直接使用刚刚在配置类中创建好的bean对象,在 [DatabaseController.java](material\springboot-knowledge\src\main\java\springboot\controller\DatabaseController.java) 中添加方法:
   ```java
   @Autowired
   @Qualifier("redisTemplateDb1")
   private StringRedisTemplate redisTemplateDb1;
   @GetMapping("/get-custom-labels")
   public String getCustomLabels() {
       StringBuilder responseStr = new StringBuilder();
       responseStr.append("<p>测试调用 springboot redis 方法开始</p>");
       // 开始操作前清空当前所在的数据库,不要误写成为 flushAll() 方法(会清空所有数据库中的数据)
       // Objects.requireNonNull() 方法表示括号内的对象不能为空,否则抛出 NullPointerException 异常
       Objects.requireNonNull(redisTemplateDb1.getConnectionFactory()).getConnection().serverCommands().flushDb();
       redisTemplateDb1.opsForValue().set("stringValue", "<p>这是字符串数据,设定其存在时间为60秒</p>");//纯字符串操作
       redisTemplateDb1.expire("stringValue", 60, java.util.concurrent.TimeUnit.SECONDS);//设置过期时间,单位秒
       responseStr.append(redisTemplateDb1.opsForValue().get("stringValue")); //获取该字符串
       redisTemplateDb1.opsForSet().add("setValue", "<p>这是set集合中的第一个数据</p>", "<p>这是set集合中的第二个数据</p>");//set集合操作,连续插入两个数据
       redisTemplateDb1.opsForSet().add("setValue", "<p>这是set集合中的第三个数据</p>");// 再插入一个数据
       Set<String> setValue = redisTemplateDb1.opsForSet().members("setValue");//获取这些数据,并遍历输出
       if (setValue != null) {
           for (String item : setValue) {
               responseStr.append(item);
           }
       }
       redisTemplateDb1.opsForList().rightPushAll("listValue", "<p>这是list集合中的第一个数据</p>", "<p>这是list集合中的第二个数据</p>");//list集合操作,从右侧插入一个数据
       redisTemplateDb1.opsForList().rightPush("listValue", "<p>这是list集合中的第二个数据</p>");//从右侧插入一个
       List<String> listValue = redisTemplateDb1.opsForList().range("listValue", 0, -1);//获取这些数据,并遍历输出
       if (listValue != null) {
           for (String item : listValue) {
               responseStr.append(item);
           }
       }
       //hash表操作,根据key和value值插入两个数据
       redisTemplateDb1.opsForHash().put("hashValue", "hashKey1", "<p>这是hash集合中的第一个数据</p>");
       redisTemplateDb1.opsForHash().put("hashValue", "hashKey2", "<p>这是hash集合中的第二个数据</p>");
       //根据hash表中的key获取value值
       responseStr.append(redisTemplateDb1.opsForHash().get("hashValue", "hashKey1"));
       responseStr.append(redisTemplateDb1.opsForHash().get("hashValue", "hashKey2"));
       responseStr.append("<p>测试调用 springboot redis 方法结束</p>");
       return responseStr.toString();
   }
   ```

   启动服务器并访问 http://localhost:8081/springboot-knowledge/database/get-custom-labels 网址,查看网页输出

2. RedisTemplate 对象的操作方法汇总:

   ```java
   redisTemplate.opsForValue();//提供了操作String类型的所有方法
   redisTemplate.opsForList();//提供了操作List类型的所有方法
   redisTemplate.opsForSet();//提供了操作Set类型的所有方法
   redisTemplate.opsForHash();//提供了操作Hash类型的所有方法
   redisTemplate.opsForZSet();//提供了操作ZSet(有序集合)类型的所有方法
   redisTemplate.expire();//设置数据过期时间
   ```
