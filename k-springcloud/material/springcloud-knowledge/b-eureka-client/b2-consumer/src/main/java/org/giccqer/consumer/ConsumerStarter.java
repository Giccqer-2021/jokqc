package org.giccqer.consumer;

import org.giccqer.consumer.config.RandomLoadBalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * spring cloud消费者客户端启动类,与提供者同理.
 * <p>@LoadBalancerClients 注解用于配置访问特定的,位于注册中心的多个实例选择其一个时所使用的负载均衡策略,
 * 其 value 值为 @LoadBalancerClient 注解数组.对于 @LoadBalancerClient 注解,其 name 属性为要访问的服务实例名,
 * configuration 属性为需要使用的负载均衡算法的class对象,二者缺一不可</p>
 * <p>@EnableFeignClients 启用 OpenFeign 通信机制.basePackages属性:要从哪个包中扫描feign api接口</p>
 */
@SpringBootApplication
@LoadBalancerClients(@LoadBalancerClient(name = "CLIENT-PROVIDER", configuration = RandomLoadBalancerConfig.class)) //注解套注解数组,这河里吗
@EnableFeignClients(basePackages = "org.giccqer.consumer.feign")
public class ConsumerStarter {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerStarter.class, args);
    }
}
