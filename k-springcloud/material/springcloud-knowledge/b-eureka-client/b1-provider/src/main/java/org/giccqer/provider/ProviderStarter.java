package org.giccqer.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring cloud提供者客户端启动类,不需要添加额外的注解,只需添加eureka客户端依赖即可.
 */
@SpringBootApplication
public class ProviderStarter {
    public static void main(String[] args) {
        SpringApplication.run(ProviderStarter.class, args);
    }
}