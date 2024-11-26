package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.entity.CompanyTable;
import springboot.service.CompanyService;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 用于与服务层和持久层交互的视图层.
 * <p>{@code @RestController} = {@code @Controller} + 所有方法中添加{@code @ResponseBody}</p>
 * <p>{@code @RequestMapping("地址前缀")} 访问以下所有方法需要添加的前缀</p>
 */
@RestController
@RequestMapping("/database")
public class DatabaseController {
    /**
     * 服务层对象实例.
     * <p>{@code @Autowired}表示自动注入对象</p>
     */
    @Autowired
    private CompanyService companyService;

    /**
     * redis模板对象，引用redis数据库所必要的对象,已在redis配置类中创建完毕.
     * <p>如果该对象处理的数据的key和value值皆以纯字符串的形式序列化传输,则可使用 StringRedisTemplate 类的实例对象且无需在配置类配置序列化方式</p>
     */
    @Autowired
    @Qualifier("defaultRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * redis模板对象，纯字符串序列化方式传输数据,在redis配置类中已经创建完毕.
     * <p>使用redis的1号数据库进行数据操作</p>
     */
    @Autowired
    @Qualifier("redisTemplateDb1")
    private StringRedisTemplate redisTemplateDb1;

    /**
     * 获取所有的员工信息,以List数组形式呈现.
     * <p>该数组最终会被包装成一个json对象</p>
     *
     * @return 返回的List结果
     */
    @GetMapping("/get-all-employees")
    public List<CompanyTable> getAllEmployeesData() {
        return companyService.getAllEmployeesData();
    }

    /**
     * 从redis缓存中获取对象信息.
     * <p>先查看redis服务器缓存中是否存在我们需要的信息,若为否,从mysql中查询并返回结果</p>
     * <p>在此过程中将数据写入缓存</p>
     *
     * @return 返回的对象转化为字符串的结果
     */
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

    /**
     * 测试 redis 对各种不同类型数据的增删改查操作.
     * <p>启用1号redis数据库而非默认的0号</p>
     *
     * @return 所有增删改查结果所组成的字符串
     */
    @GetMapping("/get-custom-labels")
    public String getCustomLabels() {
        StringBuilder responseStr = new StringBuilder();
        responseStr.append("<p>测试调用 springboot redis 方法开始</p>");
        // 开始操作前清空当前所在的数据库,不要误写成为 flushAll() 方法(会清空所有数据库中的数据)
        // Objects.requireNonNull() 方法表示括号内的对象不能为空,否则抛出 NullPointerException 异常
        Objects.requireNonNull(redisTemplateDb1.getConnectionFactory()).getConnection().serverCommands().flushDb();
        // 纯字符串操作
        redisTemplateDb1.opsForValue().set("stringValue", "<p>这是字符串数据,设定其存在时间为60秒</p>");
        // 设置过期时间,单位秒
        redisTemplateDb1.expire("stringValue", 60, java.util.concurrent.TimeUnit.SECONDS);
        responseStr.append(redisTemplateDb1.opsForValue().get("stringValue")); //获取该字符串
        // set集合操作,连续插入两个数据
        redisTemplateDb1.opsForSet().add("setValue", "<p>这是set集合中的第一个数据</p>", "<p>这是set集合中的第二个数据</p>");
        redisTemplateDb1.opsForSet().add("setValue", "<p>这是set集合中的第三个数据</p>");// 再插入一个数据
        // 获取这些数据,并遍历输出
        Set<String> setValue = redisTemplateDb1.opsForSet().members("setValue");
        if (setValue != null) {
            for (String item : setValue) {
                responseStr.append(item);
            }
        }
        // list集合操作,从右侧插入一个数据
        redisTemplateDb1.opsForList().rightPushAll("listValue", "<p>这是list集合中的第一个数据</p>", "<p>这是list集合中的第二个数据</p>");
        redisTemplateDb1.opsForList().rightPush("listValue", "<p>这是list集合中的第二个数据</p>");// 再从右侧插入一个数据
        // 获取这些数据,并遍历输出
        List<String> listValue = redisTemplateDb1.opsForList().range("listValue", 0, -1);
        if (listValue != null) {
            for (String item : listValue) {
                responseStr.append(item);
            }
        }
        // hash表操作,根据key和value值插入两个数据
        redisTemplateDb1.opsForHash().put("hashValue", "hashKey1", "<p>这是hash集合中的第一个数据</p>");
        redisTemplateDb1.opsForHash().put("hashValue", "hashKey2", "<p>这是hash集合中的第二个数据</p>");
        // 根据hash表中的key获取value值
        responseStr.append(redisTemplateDb1.opsForHash().get("hashValue", "hashKey1"));
        responseStr.append(redisTemplateDb1.opsForHash().get("hashValue", "hashKey2"));
        responseStr.append("<p>测试调用 springboot redis 方法结束</p>");
        return responseStr.toString();
    }
}
