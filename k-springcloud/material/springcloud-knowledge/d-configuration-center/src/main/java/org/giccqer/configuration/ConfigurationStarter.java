package org.giccqer.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 注册中心的Springboot启动项,与一般的Springboot启动基本项一致,只需要添加 @EnableConfigServer 注解.
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigurationStarter {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationStarter.class, args);
    }
}
