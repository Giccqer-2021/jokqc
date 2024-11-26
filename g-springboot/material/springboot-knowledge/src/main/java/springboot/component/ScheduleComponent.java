package springboot.component;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 用于测试 SpringBoot 的定时任务功能.
 * <p>需要在组件类上加入 {@code @EnableScheduling} 注解开启定时任务</p>
 */
@Component
@EnableScheduling
public class ScheduleComponent {
    /**
     * 服务器运行时,每隔6秒执行一次.
     */
    @Scheduled(fixedDelay = 6000) //单位:毫秒
    public void task() {
        System.out.println("正在执行的调度方法");
    }
}
