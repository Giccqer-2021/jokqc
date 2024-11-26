package org.giccqer.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * spring cloud服务端启动类,与一般的Springboot启动项类似,需要添加 @EnableEurekaServer 依赖.
 */
@SpringBootApplication
@EnableEurekaServer
public class ServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(ServerStarter.class, args);
    }
}
