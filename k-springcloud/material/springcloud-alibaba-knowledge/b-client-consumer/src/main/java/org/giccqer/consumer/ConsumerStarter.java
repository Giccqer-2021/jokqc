package org.giccqer.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.giccqer.consumer.feign")
public class ConsumerStarter {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerStarter.class, args);
    }
}
