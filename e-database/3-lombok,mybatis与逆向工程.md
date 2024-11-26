# lombok,mybatis与逆向工程

*本说明使用工程 [idea-mybatis知识(默认)](material\idea-mybatis-knowledge.bat)* 

## lombok使用说明

1. 导入相关依赖:对于maven直接导入即可:
   ```xml
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
   </dependency>
   ```

   对于gradle,按照以下方法导入依赖并启用注解:
   ```groovy
   implementation 'org.projectlombok:lombok'
   annotationProcessor 'org.projectlombok:lombok' //指定编译器在编译时需要使用的注解处理器
   ```

2. Lombok注解:

   |           注解           |         属性         |                             用途                             |      用于      |
   | :----------------------: | :------------------: | :----------------------------------------------------------: | :------------: |
   |          @Data           |                      | 相当于@RequiredArgsConstructor+@Setter<br />+@Getter+@ToString()+@EqualsAndHashCode |       类       |
   |         @Setter          |                      |         类中所有字段生成 setter,或某字段生成 setter          |    类,字段     |
   |         @Getter          |                      |         类中所有字段生成 getter,或某字段生成 getter          |    类,字段     |
   |         @NonNull         |                      |          类中所有字段不能为空,或某字段形参不能为空           | 类,字段,形参等 |
   |      @Synchronized       |                      |                 为某方法中所有代码添加同步锁                 |      方法      |
   |         @Cleanup         |                      |        方法结束后释放被对象占用的资源,相当于finally{}        |    局部对象    |
   |    @NoArgsConstructor    |                      |                       生成无参构造方法                       |       类       |
   |   @AllArgsConstructor    |                      |                      生成全参的构造方法                      |       类       |
   | @RequiredArgsConstructor |                      |        生成所有由final和@NonNull修饰的字段的构造方法         |       类       |
   |         @Builder         |                      | 可以使用链式方法创建对象,使用方法:<br />User user=User.builder().id(1).username().build(); |       类       |
   |       @ToString()        | exclude:排除某些字段 |                       生成toString方法                       |       类       |
   |          @Slf4j          |                      |              使本页面中的日志输出方法可以被执行              |       类       |
   |    @EqualsAndHashCode    |                      |               所有字段生成equals和hashCode方法               |       类       |

## mybatis初步架构(使用gradle)

1. 关于entity和mapper文件夹:

   > entity: 与数据表一一对应的实体类,通常用于增删改查等持久化操作,可以包括一些业务逻辑.同理,可以将文件夹名称改为 pojo (Plain Old Java Object,简单java对象,不包含业务逻辑)或 po (persistant object,持久层对象)

   > mapper: 接口映射类的包,其中的接口与resources文件夹下的mapper中的xml一一对应,也可以命名为 dao (Data Access Object,数据访问对象)
   
2. 导入 mybatis 依赖和 jdbc 依赖:

   ```groovy
   implementation 'org.mybatis:mybatis:3.5.16'
   implementation 'com.mysql:mysql-connector-j:9.0.0'
   ```

   在 idea 中推荐使用 MyBatisX 插件辅助编程

3. 在 resources 文件夹下创建 mybatis 配置文件 [mybatisConfig.xml](material\mybatis-knowledge\src\main\resources\mybatisConfig.xml) (不建议使用别的名字):
   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!-- mybatis配置文件 -->
   <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
   <configuration>
       <properties resource="database.properties"/><!--读取的properties文件的文件名,如果不需要可以不写-->
       <settings>
           <!-- 开启后,使得数据表列名(小写横线相连式命名)与java实体类属性名(小驼峰式命名)可以相互映射 -->
           <setting name="mapUnderscoreToCamelCase" value="true"/>
           <!-- 启用日志,在控制台中输出打印的sql语句 -->
           <!--<setting name="logImpl" value="STDOUT_LOGGING"/>-->
       </settings>
       <environments default="mysql">
           <environment id="mysql">
               <transactionManager type="JDBC"/><!--这些都是固定写法，标记了处理数据库的类型与环境-->
               <dataSource type="POOLED">
                   <!--导入properties文件，使用其中的参数值进行配置-->
                   <property name="driver" value="${driverClassName}"/>
                   <property name="url" value="${url}"/>
                   <property name="username" value="${username}"/>
                   <property name="password" value="${password}"/>
               </dataSource>
           </environment>
       </environments>
       <mappers>
           <!-- 配置resources文件夹下的 mapper xml 映射文件的位置 -->
           <!-- SimpleUserTableMapper 是即将使用到的mapper接口 -->
           <mapper resource="mapper\SimpleUserTableMapper.xml"/>
       </mappers>
   </configuration>
   ```

   其中对应的 database.properties 配置文件(与 [mybatisConfig.xml](material\mybatis-knowledge\src\main\resources\mybatisConfig.xml) 位于同一目录下)为:
   ```properties
   url=jdbc:mysql://localhost:3306/knowledge_database? characterEncoding=utf8&useSSL=false&serverTimezone=UTC
   driverClassName=com.mysql.cj.jdbc.Driver
   username=root
   password=123456@abcdef
   ```

4. 在 util 包下创建启用 mybatis 会话的工具类 [MybatisUtil.java](material\mybatis-knowledge\src\main\java\mybatis\util\MybatisUtil.java) ,创建 operate() 方法来打开会话,关闭会话,处理异常等:
   ```java
   public class MybatisUtil { // MapperOperator<T> 为自定义泛型接口
       public static <T> void operate(Class<T> mapperInterface, MapperOperator<T> operator, boolean commit) {
       SqlSession session = null;
       try {
           InputStream input = Resources.getResourceAsStream("mybatisConfig.xml"); //加载resources文件夹下配置文件
           SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(input);
           session = factory.openSession();
           T mapper = session.getMapper(mapperInterface);
           operator.operateSql(mapper); // 自定义的操作方法
           if (commit) session.commit(); // 是否提交数据,选择否则会话结束会回滚数据
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           if (session != null)
               session.close();
       }
       }
   }
   ```

   创建用于定义Mapper操作方法的函数式泛型接口 [MapperOperator.java](material\mybatis-knowledge\src\main\java\mybatis\util\MapperOperator.java) :
   ```java
   @FunctionalInterface //函数式接口
   public interface MapperOperator<T> {
       void operateSql(T mapper);
   }
   ```
   
5. 在 entity 包中创建实体类  [SimpleUserTable.java](material\mybatis-knowledge\src\main\java\mybatis\entity\SimpleUserTable.java) ,对应的数据表 simple_user_table 已创建好,其中的 checkName 字段和自定义的构造函数留作它用
   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class SimpleUserTable {
       private Integer userId;
       private String userName;
       private Boolean userIsMale;
       private Byte userAge;
       private Long userPhone;
       private String userAddress;
       private String userComment;
       private String checkName;//checkName 并不映射数据表内对应的任何字段,仅用作查询时输入条件.
       //自定义的构造函数,用于将非必要填入的 checkName 排除在外.
       public SimpleUserTable(Integer userId, String userName, Boolean userIsMale, Byte userAge, Long userPhone, String userAddress, String userComment) {
           this(userId, userName, userIsMale, userAge, userPhone, userAddress, userComment, null);
       }
   }
   ```

6. 在 mapper 包中创建接口  [SimpleUserTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\SimpleUserTableMapper.java) :

   ```java
   public interface SimpleUserTableMapper {
   	List<SimpleUserTable> selectAllUsers();
   }
   ```

   并在 resources 文件夹下创建 mapper 文件夹,创建xml文件 [SimpleUserTableMapper.xml](material\mybatis-knowledge\src\main\resources\mapper\SimpleUserTableMapper.xml) 映射该接口及其中的方法:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!-- 与 SimpleUserTableMapper 接口相互映射的xml文件,主要用作增删改查操作 -->
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <!-- namespace 表示该mapper接口的全路径名 -->
   <mapper namespace="mybatis.mapper.SimpleUserTableMapper">
       <!--  选择所有用户信息.可以在接口中使用 @Select() 注代替本标签.其中的 id 值要与 mapper 接口方法名一致  -->
       <select id="selectAllUsers" resultType="mybatis.entity.SimpleUserTable">
           select * from simple_user_table
       </select>
   </mapper>
   ```

7. 创建main方法执行类 [SelectOperation.java](material\mybatis-knowledge\src\main\java\mybatis\SelectOperation.java) ,使用工具类 [MybatisUtil.java](material\mybatis-knowledge\src\main\java\mybatis\util\MybatisUtil.java) 中的 operate() 方法开启mysql会话并执行该 select 方法:
   ```java
   public class SelectOperation {
   public static void main(String[] args) {
   MybatisUtil.operate(SimpleUserTableMapper.class, mapper -> {
       List<SimpleUserTable> allUsers = mapper.selectAllUsers();//查询所有数据
       System.out.println("表内所有数据:");
       allUsers.forEach(System.out::println);
   }
   }
   ```

   启动该方法,观察控制台是否能输出所有user的详细数据

8. 使用注解执行sql语言:在 [SimpleUserTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\SimpleUserTableMapper.java) 中的 selectAllUsers() 方法上添加 @Select 注解:
   ```java
   @Select("SELECT * FROM simple_user_table")
   List<SimpleUserTable> selectAllUsers();
   ```

   将对应的 [SimpleUserTableMapper.xml](material\mybatis-knowledge\src\main\resources\mapper\SimpleUserTableMapper.xml) 中的 <select> 标签移除,然后执行主方法查看运行结果

   同理,如果 entity类中字段名 与 mysql数据表字段名 完全不同,可以在 mapper接口中的方法上 使用注解 @Results 和 @Result 为二者建立映射关系,使用 @ResultType(类名.class) 来主动声明 mapper接口中的方法的返回值 所映射的entity类
   
   关于mybatis注解详见 [教学网站](https://blog.csdn.net/qq_57036151/article/details/139877021) 

## mybatis查询方法

1. 在  [SimpleUserTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\SimpleUserTableMapper.java) 接口中创建相应的方法:
   ```java
   //根据用户名查询用户.如果输入的用户名在数据库中存在多个,则返回第一个,如果不存在则返回null.
   SimpleUserTable selectUserByName(String userName);
   //使用聚合函数查询用户总数.
   int userCount();
   //查询满足某些条件的所有用户.这里使用 @Param 注解用来映射xml文件中的标识符.
       List<SimpleUserTable> selectUserByConditions(@Param("name") String userName, @Param("isMale") Boolean isMale, @Param("ageBiggerThen") Byte minAge, @Param("ageSmallerThen") Byte maxAge);
   ```

2. 在 [SimpleUserTableMapper.xml](material\mybatis-knowledge\src\main\resources\mapper\SimpleUserTableMapper.xml) 中这些方法映射标签和 sql 语句:
   ```xml
   <!-- 根据用户名查询用户信息,其中的井号 # 为参数占位符,mybatis会自动将括号内的参数值进行动态替换. $ 代表的占位符表示仅进行字符串替换,不推荐使用 -->
   <!--userName占位符名称要与对应的接口方法形参名一致,javaType表示java中的参数类型,jdbcType表示在mysql中的字段类型-->
   <select id="selectUserByName" resultType="mybatis.entity.SimpleUserTable">
       select * from simple_user_table where user_name=#{userName,javaType=String,jdbcType=VARCHAR}
   </select>
   <!-- resultType 表示返回结果对应的java对象(或基本类型),这里使用聚合函数返回数据的总数(int类型) -->
   <select id="userCount" resultType="int">
       select count(*) from simple_user_table
   </select>
   <!--根据条件查询用户信息,使用where标签包裹条件来正确处理and或or的使用,使用if标签判断条件是否为空(为空则不会添加条件)-->
   <select id="selectUserByConditions" resultType="mybatis.entity.SimpleUserTable">
       select * from simple_user_table
       <where>
           <if test="name != null and name !=''">
               user_name=#{name}
           </if>
           <if test="isMale != null"><!--mybatis能够正确处理boolean类型和tinyint(1)类型数据的转换-->
               and user_is_male=#{isMale}
           </if>
           <if test="ageBiggerThen != null">
               <!--CDATA标签不会被XML解析器解释为标记,使得xml文档正确解析大于号-->
               <![CDATA[ and user_age > #{ageBiggerThen} ]]>
           </if>
           <if test="ageSmallerThen != null">
               and user_age &lt; #{ageSmallerThen}<!-- &lt;会被解析成小于号,&gt;会被解析成大于号 -->
           </if>
       </where>
   </select>
   ```

3. 在主方法 [SelectOperation.java](material\mybatis-knowledge\src\main\java\mybatis\SelectOperation.java) 中加入以下语句,然后运行并观察控制台输出结果
   ```java
   System.out.println("查询姓名为 蔡徐坤 的数据: " + mapper.selectUserByName("蔡徐坤"));//查询姓名为蔡徐坤的数据
   System.out.println("表内数据总条数: " + mapper.userCount());//表内数据总条数
   //查询条件 性别为男，年龄在20-30之间的数据
   List<SimpleUserTable> usersByConditions = mapper.selectUserByConditions(null, true, (byte) 20, (byte) 30);
   System.out.println("查询条件 性别为男，年龄在20-30之间的数据:");
   usersByConditions.forEach(System.out::println);
   ```

## mybatis增删改方法

1. 在  [SimpleUserTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\SimpleUserTableMapper.java) 接口中创建相应的方法:
   ```java
   //创建新用户,要插入的用户的实体类中的字段名要与xml映射文件中的占位符一致.
   void insertIntoUserTable(SimpleUserTable user);
   //根据用户名修改对应用户的信息,创建方法时不可以将字符串与实体类在形参中混用.本方法使用checkName字段查询用户,根据其他字段修改用户信息.checkName字段必须写上,不能为空.
   void updateUserTable(SimpleUserTable user);
   //根据用户名删除对应的用户.
   void deleteUserByName(String UserName);
   ```

2. 在  [SimpleUserTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\SimpleUserTableMapper.java) 接口中创建相应的方法(这里不再使用<select>标签了):

   ```xml
   <insert id="insertIntoUserTable"><!--传入实体类,根据其字段名称插入对应的数据-->
       insert into simple_user_table(user_name, user_is_male,user_age,user_phone,user_address,user_comment)
       values(#{userName},#{userIsMale},#{userAge},#{userPhone},#{userAddress},#{userComment})
   </insert>
   <!--根据用户名,修改用户信息.这里使用set标签包裹条件来正确处理逗号的使用.其中传入的实体类的 checkName 字段不能为空-->
   <update id="updateUserTable">
       update simple_user_table
       <set>
           <if test="userName != null and userName !=''">
               user_name=#{userName},
           </if>
           <if test="userIsMale != null">
               user_is_male=#{userIsMale},
           </if>
           <if test="userAge != null">
               user_age=#{userAge},
           </if>
           <if test="userPhone != null">
               user_phone=#{userPhone},
           </if>
           <if test="userAddress != null">
               user_address=#{userAddress},
           </if>
           <if test="userComment != null">
               user_comment=#{userComment}
           </if>
       </set>
       where user_name=#{checkName}
   </update>
   <delete id="deleteUserByName"><!--根据用户名删除对应的用户-->
       delete from simple_user_table where user_name=#{userName}
   </delete>
   ```

3. 创建main主方法执行类 [DataAdaptOperation.java](material\mybatis-knowledge\src\main\java\mybatis\DataAdaptOperation.java) (其实没必要),执行以下方法并观察控制台输出:
   ```java
   mapper.insertIntoUserTable(new SimpleUserTable(null, "王大锤", true, (byte) 26, 23333333333L, "南京市浦西新区", null));//插入数据
   System.out.println("执行插入数据操作,其结果为: " + mapper.selectUserByName("王大锤"));//修改数据
   mapper.updateUserTable(new SimpleUserTable(null, "王晓晓", false, (byte) 28, 23333333333L, "南京市浦西新区", "巴啦啦能量.米卡哇", "王大锤"));
   System.out.println("执行更新数据操作,其结果为: " + mapper.selectUserByName("王晓晓"));
   mapper.deleteUserByName("王晓晓");//删除数据
   System.out.println("已执行删除数据操作");
   ```

## mybatis联表查询方法:

1. 本次使用数据表 employee_table 和 department_table ,二表存在外键关联,对应的实体类分别为:
    [EmployeeTable.java](material\mybatis-knowledge\src\main\java\mybatis\entity\EmployeeTable.java) 

   ```java
   @Data // employee_table 数据表中字段 employee_department_id 并没有作为本类的属性
   public class EmployeeTable {
       private Integer employeeId;
       private String employeeName;
       private DepartmentTable employeeDepartment;//需要联表查询的对象字段,该对象封装另一个数据表的数据.
   }
   ```

    [DepartmentTable.java](material\mybatis-knowledge\src\main\java\mybatis\entity\DepartmentTable.java) 
   ```java
   @Data
   public class DepartmentTable {
       private Integer departmentId;
       private String departmentName;
   }
   ```

2. 创建对应的查询语句mapper接口和xml文件:
    [EmployeeTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\EmployeeTableMapper.java) 

   ```java
   public interface EmployeeTableMapper {
       List<EmployeeTable> getAllEmployees();
   }
   ```

    [EmployeeTableMapper.xml](material\mybatis-knowledge\src\main\resources\mapper\EmployeeTableMapper.xml) 
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!-- 与 EmployeeTableMapper 接口相互映射的xml文件,主要用于联表查询 -->
   <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <mapper namespace="mybatis.mapper.EmployeeTableMapper">
       <!-- resultMap: 用于定义一个结果映射,需要在xml文件中自行声明 -->
       <select id="getAllEmployees" resultMap="EmployeeResultMap">
   	<!--在联表查询语句中,若原表和关联表中的字段名不重复则可以省略二者的表名.为了防止表名冲突,可以使用AS关键字为输出的结果字段起一个别名-->
           SELECT employee_id,employee_name,department_id,department_name FROM employee_table LEFT JOIN department_table ON employee_department_id = department_id
       </select>
       <!-- 自行声明的结果映射,id值要与resultMap的属性值相对应.type的值与实体类相对应 -->
       <resultMap id="EmployeeResultMap" type="mybatis.entity.EmployeeTable">
   <!--id标签为主键,property属性为对应的实体类属性,column属性为查询结果对应列的名字,若使用了别名则该属性也要使用别名-->
           <id property="employeeId" column="employee_id"/>
           <result property="employeeName" column="employee_name"/>
           <!-- association 标签对应于实体类中的关联对象,javaType属性为关联对象的类型 -->
           <association property="employeeDepartment" javaType="mybatis.entity.DepartmentTable">
               <id property="departmentId" column="department_id"/>
               <result property="departmentName" column="department_name"/>
           </association>
       </resultMap>
   </mapper>
   ```

   在 [mybatisConfig.xml](material\mybatis-knowledge\src\main\resources\mybatisConfig.xml) 文件中的 <mappers> 标签下添加对上面 xml 文件的读取配置:
   ```xml
   <mapper resource="mapper\EmployeeTableMapper.xml"/>
   ```

3. 创建main方法的执行类  [UniteTablesOperation.java](material\mybatis-knowledge\src\main\java\mybatis\UniteTablesOperation.java) (其实没必要),写入以下语句并执行:

   ```java
   System.out.println("执行联合查询操作,其结果为: ");
   mapper.getAllEmployees().forEach(System.out::println);
   ```

## mybatis逆向工程

1. 本次使用一张新的数据表 dormitory_table 来进行 entity类,mapper接口 自动生成的操作
   
2. 在 resources 文件夹下创建 [generatorConfig.xml](material\mybatis-knowledge\src\main\resources\generatorConfig.xml) 配置文件(不建议使用别的名字):
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!-- mybatis逆向工程配置文件 -->
   <!DOCTYPE generatorConfiguration
           PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
           "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
   <generatorConfiguration>
       <properties resource="database.properties"/><!--读取的properties文件的文件名,如果不需要可以不写-->
       <context id="DB2Tables" targetRuntime="MyBatis3">
           <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin"/>
           <commentGenerator><!--去除注释-->
               <property name="suppressAllComments" value="true"/>
           </commentGenerator><!-- 以下为数据库连接信息 -->
           <jdbcConnection driverClass="${driverClassName}"
                           connectionURL="${url}"
                           userId="${username}"
                           password="${password}">
           </jdbcConnection>
           <!-- entity实体类生成位置 -->
           <javaModelGenerator targetPackage="mybatis.entity" targetProject="src\main\java">
               <property name="enableSubPackages" value="true"/><!-- enableSubPackages:是否让schema作为包的后缀 -->
               <property name="trimStrings" value="true"/><!-- 从数据库返回的值被清理前后的空格 -->
           </javaModelGenerator>
           <sqlMapGenerator targetPackage="mapper" targetProject="src\main\resources"><!-- xml映射文件生成位置 -->
               <property name="enableSubPackages" value="true"/>
           </sqlMapGenerator>
           <!-- mapper接口生成位置 -->
           <javaClientGenerator type="XMLMAPPER" targetPackage="mybatis.mapper" targetProject="src\main\java">
               <property name="enableSubPackages" value="true"/>
           </javaClientGenerator>
           <!-- 逆向分析的表 -->
           <!-- tableName设置为 * 号，可以对应所有表，此时不写domainObjectName属性 -->
           <!-- domainObjectName属性指定生成出来的实体类的类名 -->
           <table tableName="dormitory_table" domainObjectName="DormitoryTable"/>
       </context>
   </generatorConfiguration>
   ```

3. 使用以下两种方法之一来创建数据表对应的 实体类和mapper映射的接口及xml文件 :

   - 使用java方法:导入逆向工程依赖:
     ```groovy
     implementation 'org.mybatis.generator:mybatis-generator-core:1.4.2'
     ```

     在 [MybatisUtil.java](material\mybatis-knowledge\src\main\java\mybatis\util\MybatisUtil.java) 工具类中创建逆向工程方法,本方法基本上为固定写法照写就好:
     ```java
     public static void generateEntityAndMapper() throws Exception {
         List<String> warnings = new ArrayList<>();
         boolean overwrite = true;
         File configFile = new File("src/main/resources/generatorConfig.xml"); // 逆向工程配置文件路径
         ConfigurationParser cp = new ConfigurationParser(warnings);
         Configuration config = cp.parseConfiguration(configFile);
         DefaultShellCallback callback = new DefaultShellCallback(overwrite);
         MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
         myBatisGenerator.generate(null);
         System.out.println("mybatis逆向工程文件生成成功");
     }
     ```

     在测试方法中创建  [GeneratorTest.java](material\mybatis-knowledge\src\test\java\mybatis\GeneratorTest.java) 类执行上述方法:
     ```java
     public class GeneratorTest {
         @Test //不要闲的没事就执行这个方法,它会改写你不想改写的代码
         public void generate() throws Exception {
             MybatisUtil.generateEntityAndMapper();
         }
     }
     ```

     执行该方法后,查看包 entity,mapper 中是否生成实体类,example类和mapper接口,以及查看 resources 文件夹下是否生成对应的 xml 映射文件

   - (仅限使用maven构建工具)使用构建工具直接创建:在  pom.xml 文件中<build><plugins>标签里添加逆向工程**插件**:
     ```xml
     <plugin>
         <groupId>org.mybatis.generator</groupId>
         <artifactId>mybatis-generator-maven-plugin</artifactId>
         <version>1.4.2</version>
         <configuration>
             <verbose>true</verbose>
             <overwrite>true</overwrite>
         </configuration>
         <dependencies><!--该依赖声明必不可少-->
             <dependency>
                 <groupId>com.mysql</groupId>
                 <artifactId>mysql-connector-j</artifactId>
                 <version>9.0.0</version>
             </dependency>
         </dependencies>
     </plugin>
     ```

     执行 mybatis-generator 对应的 mvn 插件命令,然后查看包 entity,mapper 中是否生成实体类,example类和mapper接口,以及查看 resources 文件夹下是否生成对应的 xml 映射文件
     
   - 注意:若使用gradle则不建议直接使用构建工具本身创建文件

4. 文件创建后,应根据实际情况做出一些调整:
   entity包下  [DormitoryTable.java](material\mybatis-knowledge\src\main\java\mybatis\entity\DormitoryTable.java) :添加 @Data @AllArgsConstructor @NoArgsConstructor 注解并移除多余的 getter setter 方法(这样声明了全参和无参构造方法以便于之后创建实例)
   entity包下  [DormitoryTableExample.java](material\mybatis-knowledge\src\main\java\mybatis\entity\DormitoryTableExample.java) :根据idea提示删除冗余方法并添加注解关键字等
   mapper包下  [DormitoryTableMapper.java](material\mybatis-knowledge\src\main\java\mybatis\mapper\DormitoryTableMapper.java) :并没有什么改动
   resources/mapper文件夹下的xml:  [DormitoryTableMapper.xml](material\mybatis-knowledge\src\main\resources\mapper\DormitoryTableMapper.xml) :并没有什么改动
   在 [mybatisConfig.xml](material\mybatis-knowledge\src\main\resources\mybatisConfig.xml) 文件中的 <mappers> 标签下添加相应的读取配置:

   ```xml
   <mapper resource="mapper\DormitoryTableMapper.xml"/>
   ```

   在 springboot 中并没有这样的配置文件,但要在 mapper 接口上添加 @Mapper 和 @Repository 注解

5. 创建main方法的执行类 [GeneratorProjectOperation.java](material\mybatis-knowledge\src\main\java\mybatis\GeneratorProjectOperation.java) (其实没必要),测试以下方法是否能运行成功:
   ```java
   System.out.println("id为1的宿舍学生信息: " + mapper.selectByPrimaryKey(1));// 根据主键查询某条数据
   ```

6. 详细的mybatis逆向工程文件生成后的使用方法如下:
   ```java
   DormitoryTableExample selectExample = new DormitoryTableExample();//自定义example类配置查询条件,根据该条件查询
   selectExample.setDistinct(true); // 查询结果去除重复数据
   DormitoryTableExample.Criteria selectCriteria = selectExample.createCriteria();
   selectCriteria.andIsMaleBetween((byte) 1, (byte) 1).andNotesLike("%李狗蛋%"); // 查询男性,其笔记中包含"李狗蛋"三字
   System.out.println("使用example类查询结果: " + mapper.selectByExample(selectExample)); // 返回查询结果
   // 插入数据,然后查询插入的数据
   mapper.insert(new DormitoryTable(null, "王小锤", (byte) 1, 88888888880L, "八十,八十,八十!"));
   DormitoryTableExample insertExample = new DormitoryTableExample();
   insertExample.createCriteria().andStudentNameEqualTo("王小锤");
   System.out.println("新插入的数据: " + mapper.selectByExample(insertExample));
   //修改刚刚插入的数据,然后查询.updateByExampleSelective表示只修改非空的字段,updateByExample修改所有字段(会赋予空值)
   mapper.updateByExampleSelective(new DormitoryTable(null, "李大铲", null, 66666666660L, null), insertExample);
   DormitoryTableExample upDateExample = new DormitoryTableExample();
   upDateExample.createCriteria().andStudentNameEqualTo("李大铲");
   System.out.println("更新后的数据: " + mapper.selectByExample(upDateExample));
   mapper.deleteByExample(upDateExample);//删除刚刚修改的数据
   System.out.println("已删除数据");
   ```

