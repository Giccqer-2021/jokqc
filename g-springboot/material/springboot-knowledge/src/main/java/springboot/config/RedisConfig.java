package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * redis 配置类.
 */
@Configuration
public class RedisConfig {
    /**
     * 配置默认的 redis 序列化方式(json).
     * <p>使用外部注入的 {@link RedisConnectionFactory} 类型的对象注入该方法的形参,该对象会根据 application.yml 文件中的参数进行默认配置,
     * 在此基础上只需要修改部分内容即可.</p>
     * <p>{@code @Bean()} 注解声明了该返回值对象在 springIOC 容器中的名称,引用该对象时要写入该名称</p>
     *
     * @param redisConnectionFactory redis 连接工厂对象,自动注入
     * @return RedisTemplate 对象,注入 springIOC 容器中
     */
    @Bean("defaultRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置键值序列化方式
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        //设置值json序列化方式,使用 Jackson2JsonRedisSerializer 代替默认的java序列化方式
        //若要使用字符串序列化方式则使用 StringRedisSerializer
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //key hashmap序列化
        redisTemplate.setHashKeySerializer(redisTemplate.getStringSerializer());
        //value hashmap序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //在bean属性设置完成之后执行一些特定的操作
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置启用1号数据库(默认0号)的redis连接工厂对象.
     * <p>简洁起见,本次直接使用已配置好字符串序列化方式的类 {@code StringRedisTemplate} 的对象作为返回值,在此基础上进行自定义配置</p>
     * <p>本次使用新的连接工厂对象进行配置,默认的 application.yml 中的配置将不会对该返回的对象生效</p>
     *
     * @return StringRedisTemplate 对象,注入 springIOC 容器中
     */
    @Bean("redisTemplateDb1")
    public StringRedisTemplate redisTemplateDb1() {
        // 配置连接工厂对象
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
        configuration.setDatabase(1); // 设置要使用的数据库
        configuration.setPassword("123456@abcdef"); // 设置密码
        // 配置连接客户端对象,该对象由于没有自定义的任何配置可以省略
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder().build();
        // 配置连接工厂对象
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration, clientConfiguration);
        connectionFactory.start(); // 不写就报错
        return new StringRedisTemplate(connectionFactory); //StringRedisTemplate 是 RedisTemplate 的子类
    }
}
