package org.giccqer;

import io.netty.buffer.ByteBuf;
import org.apache.ibatis.session.SqlSession;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用来测试依赖传递与导入是否正确的主方法.
 */
public class SubModuleMain {
    /**
     * 该常量用以测试子模块之间的依赖引用.
     */
    public static final String SUB_MODULE_STRING = "子模块公有常量";

    public static void main(String[] args) {
        //netty依赖已在父标签中强制声明使用
        System.out.println("检测 netty 依赖是否导入成功: " + ByteBuf.class.getName());
        //mybatis依赖在子标签中根据父依赖中声明的dependencyManagement标签内的依赖自行导入
        System.out.println("检测 mybatis 依赖是否导入成功: " + SqlSession.class.getName());
        //springboot-redis依赖在子标签中根据父依赖的父依赖中声明的dependencyManagement标签内的依赖自行导入
        System.out.println("检测 springboot-redis 依赖是否导入成功: " + StringRedisTemplate.class.getName());
        //spring-cloud-starter-netflix-eureka-server依赖在子标签中根据父依赖从另一个父依赖纳入的dependencyManagement标签内的依赖自行导入
        System.out.println("检测 spring-cloud-starter-netflix-eureka-server 依赖是否导入成功:" + EnableEurekaServer.class.getName());
    }
}