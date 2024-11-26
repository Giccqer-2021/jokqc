package org.giccqer.consumer.config;

import feign.Logger.Level;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 与OpenFeign相关的种种配置.
 */
@Configuration
public class FeignConfig {
    /**
     * 启用feign日志的配置,启用后可清晰观察消费者使用feign向提供者相互传递的请求和响应状况.
     *
     * @return 日志级别配置对象
     */
    @Bean
    public Level feignLoggerLevel() {
        return Level.FULL; //打印请求体和响应体的头部,内容和元数据
    }

    /**
     * 该配置详细阐明了当向提供者发送请求发生错误时,该抛出什么异常.
     * <p>形参 methodKey 表示出现错误的FeignApi中的完整方法名,形参 response 表示从提供者那里返回的包含错误状态码的响应体</p>
     *
     * @return 错误解码器对象
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            System.out.println("出现异常,出现异常的方法是: " + methodKey + ",返回的请求体参数:\n" + response);
            //若想得知返回体的错误信息,需要先获取相关的输入流,将流转化为字符串后进行输出
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().asInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("返回的请求体内容为: " + stringBuilder);
            //根据响应体中不同的状态码返回不同的异常
            return switch (response.status()) {
                case 400 -> new RuntimeException("请求参数错误,状态码:400");
                case 403 -> new RuntimeException("请求被阻止,状态码:403");
                case 404 -> new RuntimeException("请求页面不存在,状态码:404");
                case 405 -> new RuntimeException("请求方法不允许,状态码:405");
                case 500 -> new RuntimeException("服务器内部错误,状态码:500");
                default -> new RuntimeException("其他异常,状态码:" + response.status());
            };
        };
    }

    /**
     * 当服务器内部链接超时时的重试策略.
     *
     * @return 重试策略的配置对象, 会被feign自动识取
     */
    @Bean
    public Retryer retryer() {
        //参数1:重试间隔时间(毫秒),参数2:重试最大间隔时间(毫秒),参数3:最大重试次数
        return new Retryer.Default(100, 1000, 3);
    }
}
