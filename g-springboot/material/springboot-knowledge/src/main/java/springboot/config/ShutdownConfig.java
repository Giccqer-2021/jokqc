package springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * 服务器关闭时要执行的某些操作.
 * <p>需要创建一个 {@code ApplicationListener<ContextClosedEvent>} 对象注入到容器中</p>
 * <p>{@code ApplicationListener} 为springboot的事件监听器,用来监听某些过程</p>
 * <p>这里模拟了关闭服务器时清理redis数据库的操作</p>
 */
@Configuration
public class ShutdownConfig implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    @Qualifier("redisTemplateDb1")
    private StringRedisTemplate redisTemplateDb1;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("正在执行关闭服务器操作...");
        Set<String> keys = redisTemplateDb1.keys("*"); //获取所有键
        if (keys != null) { //遍历并删除
            keys.forEach(key -> redisTemplateDb1.delete(key));
        }
        System.out.println("服务器已关闭");
    }
}
