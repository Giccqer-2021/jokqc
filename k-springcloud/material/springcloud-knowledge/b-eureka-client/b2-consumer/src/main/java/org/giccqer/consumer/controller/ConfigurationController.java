package org.giccqer.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本controller层专门用于测试自定义配置的加载(从配置中心,或本地配置文件).
 * <p>要读取的配置文件为配置中心的 custom-cars.yml</p>
 * <p>注解 @RefreshScope 表名启用手动刷新模式</p>
 */
@RefreshScope
@RestController
public class ConfigurationController {
    /**
     * &#064;Value() 注解表名加载配置文件中的对应配置并注入该字符串.
     */
    @Value("${custom-cars.train}")
    private String config1;

    @Value("${custom-cars.automobile}")
    private String config2;

    @Value("${custom-cars.plane}")
    private String config3;

    /**
     * Environment 对象通常用于存储应用程序的环境配置信息,例如数据库连接字符串,API密钥等.
     */
    @Autowired
    private Environment env;

    /**
     * 使用两种不同的方法,获取本实例的相关配置,并返回给前端.
     *
     * @return 配置参数的详细信息
     */
    @GetMapping("/get-remote-config")
    public String getRemoteConfig() {
        String remoteConfig = "配置参数1: " + config1 + " ,配置参数2: " + config2 + " ,配置参数3: " + config3;
        String environmentConfig = "配置参数1: " + env.getProperty("custom-cars.train") + " ,配置参数2: " +
                env.getProperty("custom-cars.automobile") + " ,配置参数3: " + env.getProperty("custom-cars.plane");
        return "<p>使用注解注入得到的参数配置:<br>" + remoteConfig + "<br>使用Environment对象得到的参数配置:<br>" + environmentConfig + "</p>";
    }
}
