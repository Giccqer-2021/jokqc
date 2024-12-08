package org.giccqer;

import org.giccqer.dto.CustomerDto;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 根项目的启动类,一般父项目是不添加src源码的.
 */
public class FatherStarter {
    public static void main(String[] args) {
        CustomerDto customerDto = new CustomerDto("张三", true, "北京", 1234567890L, "zhangsan@163.com");
        //Springboot相关依赖在父项目中导入完毕
        System.out.println("检测 Springboot 依赖是否导入成功: " + SpringBootApplication.class.getName());
        //测试 Lombok 注解能否被正确解析:
        System.out.println("检测 Lombok 注解是否能被正确解析: " + customerDto);
    }
}
