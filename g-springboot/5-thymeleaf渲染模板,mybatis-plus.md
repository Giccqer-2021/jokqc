# thymeleaf渲染模板,mybatis-plus

## 后端渲染模板thymeleaf

1. 在 resources 文件夹下创建 static 和 templates 文件夹用来存放 静态资源文件 和 thymeleaf模板 ,下载 [jquery](https://jquery.com/) 并将其置入 static 中

2. 在  [pom.xml](material\springboot-knowledge\pom.xml) 导入thymeleaf模板依赖:

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-thymeleaf</artifactId>
   </dependency>
   ```

3. 在视图层类  [DatabaseController.java](material\springboot-knowledge\src\main\java\springboot\controller\DatabaseController.java) 中写入:

   ```java
   @GetMapping("/thymeleaf/hello")
   public String thymeleafPage(Model model) {
       model.addAttribute("CustomInfo", "这是自定义的属性");
       return "hello";
   }
   ```

4. 根据 [templates素材文件](material\thymeleaf.html) ,在 resources\templates 中创建  [hello.html](material\springboot-knowledge\src\main\resources\templates\hello.html) 模板:

   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <!--thymeleaf模板，以上声明为固定写法-->
   <head>
       <meta charset="UTF-8">
       <title>thymeleaf测试页面</title>
       <!--@标签表示引入静态资源位置，这里的静态资源是放在static文件夹下的文件-->
       <!--括号内的路径开头要加上一个斜杠/-->
       <script type="text/javascript" th:src="@{/jquery-3.7.1.min.js}"></script>
   </head>
   <body>
   <p>加载thymeleaf模板成功</p>
   <br>
   <p th:text="${CustomInfo}"></p><!-- th:text 表示进行文本替换，${…}表示对某变量直接进行取值 -->
   <button id="CustomButton">点我测试js代码</button>
   <script>
       $("#CustomButton").click(function() {alert("按钮被点击");});
   </script>
   <!--如需要则取消注解-->
   <!--<p>这个标签用来测试thymeleaf网页热加载情况</p>-->
   </body>
   </html>
   ```

5. (可选)在  [application.yml](material\springboot-knowledge\src\main\resources\application.yml) 中添加相关配置:

   ```yml
   spring:
     thymeleaf: #thymeleaf视图模板配置
       cache: false #服务器运行时是否启用缓存,默认为true,开发阶段应关闭以允许动态刷新
       check-template: true #模板映射时先检查模板是否存在,默认为true
       prefix: classpath:/templates/ #视图映射时需要在映射地址前添加的字符串(文件夹路径),默认为 classpath:/templates/
       suffix: .html #进行视图映射时需要在映射地址后添加的字符串,一般（且默认）是文件后缀名 .html
   ```

6. 启动服务器,输入网址 http://localhost:8081/springboot-knowledge/thymeleaf/hello 检查效果

7. 对于 thymeleaf 网页的内容可以在服务器不重启的情况下热修改,只需要在修改后重新构建 (Ctrl+F9) 然后刷新页面即可

## mybatis-plus(gradle构建)

### 基础架构

*本说明使用工程 [idea-mybatis-plus知识](material\idea-mybatis-plus-knowledge.bat)* 

1. 使用 MyBatis-Plus 后,可以在不书写任何 mapper 映射接口的方法和其对应 sql 语句的情况下仍可以实现任何状态下的增删改查等业务操作,添加以下依赖将其加入工程:
   ```groovy
   implementation 'com.baomidou:mybatis-plus-spring-boot3-starter:3.5.7'
   implementation 'com.mysql:mysql-connector-j:9.0.0'
   ```

   同时删除以往的 MyBatis 依赖,因为 MyBatis-Plus 本身包含了它

2. 其配置方式与mybatis基本相同,在 [application.yml](material\mybatis-plus-Knowledge\src\main\resources\application.yml) 写入以下配置
   ```yml
   spring:
     datasource:
       username: root
       password: 123456@abcdef
       url: jdbc:mysql://localhost:3306/knowledge_database? characterEncoding=utf8&useSSL=false&serverTimezone=UTC
       driver-class-name: com.mysql.cj.jdbc.Driver
   mybatis-plus:
     global-config: # 全局配置
       db-config: # 数据库配置
         table-prefix: # 所有表名的前缀,本例什么都没有
         id-type: auto # 主键自增,默认是 ASSIGN_ID 使用雪花算法生成 Long 类型的全局唯一主键
   ```

3. 创建实体类 [CompanyTable.java](material\mybatis-plus-Knowledge\src\main\java\springboot\entity\CompanyTable.java) ,对应数据表 company_table 其中匹配是指表名或表中字段名以小写下划线相连的方式命名且与类名或变量名的驼峰命名法相互映射的一种状态, Mybatis-Plus能自动识别这一点,无需在 application. yml 文件中另行配置:

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @TableName("company_table") //@TableName 注解表明了该实体类对应的数据表,可以省略(前提是表名与类名能够匹配)
   public class CompanyTable {
       //IdType.AUTO 表明主键为自增型主键,默认情况下,如果不加该注解则Mybatis-Plus会将变量名与表中字段名相互匹配且为 id 或 userId 的 Integer 或 Long 类型的字段作为主键,其默认自增方式为 IdType.ASSIGN_ID 自带的雪花算法
       @TableId(value = "employee_id", type = IdType.AUTO)
       private Integer employeeId;
       @TableField(value = "employee_name") //@TableField 在表字段名与类名不匹配的时候可以用这个注解声明
       private String employeeName;
       private Boolean isMale;
       private Long employeePhone;
       private String notes;
   }
   ```

   与 MyBatis 不同的是,许多声明映射的注解并不是写在 mapper 接口中而是直接写在实体类中的,这些注解有:

   |     注解      |                             属性                             |                             用途                             | 用于 |
   | :-----------: | :----------------------------------------------------------: | :----------------------------------------------------------: | :--: |
   | @TableName()  |                          对应的表名                          |                  用于声明哪张表映射该实体类                  |  类  |
   |  @TableId()   | value:对应的主键字段名<br />type:主键的自增策略(默认雪花算法) |       用于声明哪个变量是主键及其<br />对应表中哪个字段       | 字段 |
   | @TableField() |                    该变量对应表中的字段名                    |                 声明该变量与表中哪个字段映射                 | 字段 |
   |  @TableLogic  |                                                              | 声明该变量为表中逻辑删除标志变量<br />一般为Integer类型,0为未删除,1为已删除 | 字段 |
   |    @Value     |                                                              | 声明该变量为乐观锁标志变量,该变量会在<br />数据更新时被读取和更新 | 字段 |

4. 创建对应的 mapper 映射接口 [CompanyTableMapper.java](material\mybatis-plus-Knowledge\src\main\java\springboot\mapper\CompanyTableMapper.java) ,该接口需继承 BaseMapper<实体类类名> 接口后方可在编程时使用 Mybatis-Plus 中的方法:
   ```java
   @Mapper
   @Repository
   public interface CompanyTableMapper extends BaseMapper<CompanyTable> {
   	//这里可以不用写任何抽象方法
   }
   ```

5. 创建测试类 [MapperTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\MapperTest.java) ,使用 Mybatis-Plus 提供的方法测试查询并打印数据表中的所有数据:
   ```java
   @SpringBootTest
   public class MapperTest {
       @Autowired
       private CompanyTableMapper companyTableMapper;
       public void connectionTest() {
       List<CompanyTable> companyList = companyTableMapper.selectList(null);//selectList()中为null时返回所有数据
       System.out.println("mapper循环打印数据表信息: ");
       companyList.forEach(System.out::println);
   }
   }
   ```

### mapper的基本增删改查操作

在测试类 [MapperTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\MapperTest.java) 中分别演示这些方法:

查询方法:
```java
CompanyTable companyID1 = companyTableMapper.selectById(1); //根据主键id查询(id类型不一定是数字)
System.out.println("id为1的数据: " + companyID1);
//根据id所组成的集合查询
List<CompanyTable> companiesID123 = companyTableMapper.selectBatchIds(Arrays.asList(1, 2, 3));
System.out.println("id为1,2,3的数据: ");
companiesID123.forEach(System.out::println);
HashMap<String, Object> selectCondition = new HashMap<>();//使用hashmap设定查询条件
selectCondition.put("employee_name", "王晓晓");
List<CompanyTable> companyList = companyTableMapper.selectByMap(selectCondition); //根据该查询条件查询
System.out.println("姓名为王晓晓的数据: " + companyList.getLast());
```

插入数据方法:
```java
CompanyTable companyTable = new CompanyTable(null, "马牛逼", true, 1234567890L, "这人敢吃冰淇淋");
int result = companyTableMapper.insert(companyTable);
System.out.println("插入数据条数: " + result);
```

修改数据方法(使用刚刚插入的数据):
```java
HashMap<String, Object> selectCondition = new HashMap<>(); //使用HashMap输入查询条件
selectCondition.put("employee_name", "马牛逼");// 由于主键为自增主键,所以并不清楚该数据在表中的id,需要通过查询获得
int id = companyTableMapper.selectByMap(selectCondition).getLast().getEmployeeId();
CompanyTable companyTable = new CompanyTable(id, "马牛逼", false, 9000000000L, null);
companyTableMapper.updateById(companyTable);
System.out.println("数据修改后的值: " + companyTableMapper.selectById(id));
```

删除数据方法(删除刚刚插入的数据):
```java
HashMap<String, Object> selectCondition = new HashMap<>(); //使用HashMap输入查询条件
selectCondition.put("employee_name", "马牛逼");
int deleteCount = companyTableMapper.deleteByMap(selectCondition);
System.out.println("已删除的信息条数: " + deleteCount);
```

### mapper自定义sql语句

与一般的 MyBatis 操作方法相同,直接在 [CompanyTableMapper.java](material\mybatis-plus-Knowledge\src\main\java\springboot\mapper\CompanyTableMapper.java) 接口中创建对应方法:
```java
List<CompanyTable> selectAll();
```

在 resources/mapper 文件夹中创建 xml 映射文件 [CompanyTableMapper.xml](material\mybatis-plus-Knowledge\src\main\resources\mapper\CompanyTableMapper.xml) :
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="springboot.mapper.CompanyTableMapper">
    <select id="selectAll" resultType="springboot.entity.CompanyTable">
        select * from company_table
    </select>
</mapper>
```

在  [application.yml](material\mybatis-plus-Knowledge\src\main\resources\application.yml) 文件中添加对应的映射声明:
```yml
mybatis-plus:
  mapper-locations: mapper/*.xml
```

在测试类 [MapperTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\MapperTest.java) 中测试运行该方法:
```java
System.out.println("自定义select语句查询并循环打印结果:");
companyTableMapper.selectAll().forEach(System.out::println);
```

### mybatis-plus包装器Wrapper用法

创建测试类 [WrapperTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\WrapperTest.java) ,测试创建的第一个查询全数据的方法:
```java
@SpringBootTest
public class WrapperTest {
@Autowired
private
CompanyTableMapper companyTableMapper;
@Test
public void wrapperUseTest() {
    QueryWrapper<CompanyTable> wrapper = Wrappers.query(); //创建查询条件,等同于 new QueryWrapper<>();
    List<CompanyTable> companyTables = companyTableMapper.selectList(wrapper);
    System.out.println("根据 QueryWrapper 对象声明的查询条件输出所有数据: ");
    companyTables.forEach(System.out::println); //由于没有定义任何条件,输出所有数据
}
}
```

以下为使用 QueryWrapper 查询包装器进行查询的示例:
```java
QueryWrapper<CompanyTable> wrapper = new QueryWrapper<>();
wrapper.select("employee_id", "employee_phone", "employee_name"); //只查询指定字段
wrapper.like("notes", "%偷懒%") //模糊查询
        .gt("employee_id", 1) //小于
        .lt("employee_id", 4) //大于
        .eq("is_male", true) //等于
        .or() //或,除了这里其他语句之间默认加 and 连接
        .eq("employee_phone", "12323234542"); //等于
List<CompanyTable> companyTables = companyTableMapper.selectList(wrapper);
System.out.println("根据 QueryWrapper 对象声明的查询条件输出所有数据: ");
companyTables.forEach(System.out::println);
```

以下为使用 UpdateWrapper 修改包装器进行数据修改的示例:
```java
//插入一条数据,然后查询其获得的id
CompanyTable insertTable = new CompanyTable(null, "橡胶君", true, 99988811122L, "大香蕉一条大香蕉");
companyTableMapper.insert(insertTable);
HashMap<String, Object> selectCondition = new HashMap<>();
selectCondition.put("employee_name", "橡胶君");
CompanyTable resultTable = companyTableMapper.selectByMap(selectCondition).getFirst();
System.out.println("已插入数据: " + resultTable);
int id = resultTable.getEmployeeId();
//这里使用 Wrappers.update() 创建 UpdateWrapper 对象用来更新指定条件对的数据
UpdateWrapper<CompanyTable> wrapper = Wrappers.update();
wrapper.eq("employee_name", "橡胶君"); //指定要更新的数据的条件
wrapper.set("employee_name", "橡胶君2"); //要更新的数据
wrapper.set("employee_phone", 99988811122L); //要更新的数据
wrapper.set("notes", null); //要更新的数据
//更新数据,由于前者为null所以要删除数据的条件和更新数据的方法都写在了 wrapper 中
companyTableMapper.update(null, wrapper);
System.out.println("第一次更新后的数据: " + companyTableMapper.selectById(id));
UpdateWrapper<CompanyTable> wrapper2 = Wrappers.update();
CompanyTable checkTable = new CompanyTable(null, "橡胶君3", false, 12312341234L, "我在东北玩泥巴");
wrapper2.eq("employee_name", "橡胶君2");
// 以后者(wrapper)为更新数据的条件,前者实体类对象为更新数据的内容,执行更新数据操作
companyTableMapper.update(checkTable, wrapper2);
System.out.println("第二次更新后的数据: " + companyTableMapper.selectById(id));
companyTableMapper.deleteById(id); //删除数据
```

### mybatis-plus分页查询

1. 创建相关配置类 [MybatisPlusConfig.java](material\mybatis-plus-Knowledge\src\main\java\springboot\config\MybatisPlusConfig.java) ,固定写法照写便是:
   ```java
   @Configuration
   public class MybatisPlusConfig {
       @Bean
       public MybatisPlusInterceptor mybatisPlusInterceptor() {
           MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
           interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
           return interceptor;
       }
   }
   ```

2. 创建测试类 [PageTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\PageTest.java) ,测试查询并输出结果:
   ```java
   @SpringBootTest
   public class PageTest {
       @Autowired
       private CompanyTableMapper companyTableMapper;
       @Test
       public void getPageTest() {
           Page<CompanyTable> page = new Page<>(1, 2);// 当前页码，每页显示条数
           companyTableMapper.selectPage(page, null);
           List<CompanyTable> records = page.getRecords();
           System.out.println("循环打印第一页结果: ");
           records.forEach(System.out::println);
           System.out.println("当前页页码：" + page.getCurrent());
           System.out.println("每页显示条数：" + page.getSize());
           System.out.println("总数据条数：" + page.getTotal());
           System.out.println("总页数：" + page.getPages());
           System.out.println("是否有上一页：" + page.hasPrevious());
           System.out.println("是否有下一页：" + page.hasNext());
       }
   }
   ```

### mybatis-plus服务层方法

1. 创建服务层接口 [CompanyService.java](material\mybatis-plus-Knowledge\src\main\java\springboot\service\CompanyService.java) ,继承 IService<实体类类名> 接口后方可在编程时使用 Mybatis-Plus 中的方法:
   ```java
   public interface CompanyService extends IService<CompanyTable> {
       //这里可以什么都不用写
   }
   ```

2. 创建该接口的实现类  [ComponyServiceImpl.java](material\mybatis-plus-Knowledge\src\main\java\springboot\service\impl\ComponyServiceImpl.java) ,除了继承该接口还需继承 ServiceImpl<Mapper接口名,实体类类名> 抽象类,该抽象类中没有任何抽象方法,但是全部重写了继承接口所继承的接口中的所有方法,因此本类可以不主动实现任何方法
   ```java
   @Service
   public class ComponyServiceImpl extends ServiceImpl<CompanyTableMapper, CompanyTable> implements CompanyService { //这里可以什么都不用写
   }
   ```

3. 创建测试类 [ServiceTest.java](material\mybatis-plus-Knowledge\src\test\java\springboot\ServiceTest.java) 使用该实现类中 MyBatis-Plus 提供的方法查询表中所有数据:
   ```java
   @SpringBootTest
   public class ServiceTest {
       @Autowired
       private CompanyService companyService;
       @Test
       public void connectionTest() {
           System.out.println("service循环打印数据表信息: ");
           companyService.list().forEach(System.out::println);
       }
   }
   ```

4. 使用服务层对象进行增删改查操作的具体方法示例:
   ```java
   CompanyTable insertTable = new CompanyTable(null, "大尾巴", true, null, "阳光彩虹小白马");
   companyService.save(insertTable); //根据实体类对象插入数据
   QueryWrapper<CompanyTable> newTableWrapper = new QueryWrapper<>();
   newTableWrapper.eq("employee_name", "大尾巴");
   CompanyTable resultTable = companyService.getOne(newTableWrapper);
   System.out.println("新增后的数据信息: " + resultTable);
   int id = resultTable.getEmployeeId(); //查询得到插入的数据id
   CompanyTable updateTable = new CompanyTable(id, "小尾巴", false, 12111223344L, null);
   companyService.saveOrUpdate(updateTable); //根据实体类对象的id修改数据,若id不存在则改为插入数据
   System.out.println("修改后的数据信息: " + companyService.getById(id));
   QueryWrapper<CompanyTable> selectWrapper = Wrappers.query(); //创建查询条件对象
   selectWrapper.in("employee_id", 1, 2, id);
   System.out.println("循环输出id为1,2和刚刚新增的数据: ");
   companyService.list(selectWrapper).forEach(System.out::println); //根据QueryWrapper对象的查询条件循环输出查询结果
   System.out.println("删除刚刚新增的数据");
   companyService.removeById(id); //根据主键删除数据
   ```

5. 其他 MyBatis-Plus 相关教程详见 [mybatis-plus 官网教程](https://baomidou.com/introduce/) 或 [mybatis-plus CSDN教程](https://blog.csdn.net/Bb15070047748/article/details/129212543) 
