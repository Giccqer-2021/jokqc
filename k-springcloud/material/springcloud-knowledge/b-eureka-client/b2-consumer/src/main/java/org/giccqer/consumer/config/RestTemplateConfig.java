package org.giccqer.consumer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * RestTemplate配置类.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 配置RestTemplate,在controller中使用它主动发送请求.
     * <p>必须添加 @LoadBalanced 注解,添加后 restTemplate 可以通过服务名来调用其他服务,而不需要指定具体的服务实例的地址.</p>
     * <p>spring Cloud 会根据服务名自动选择可用的服务实例,并进行负载均衡</p>
     *
     * @return RestTemplate实例对象
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory(); //使用工厂类配置对象
        factory.setReadTimeout(5000); //连接超时时间,单位毫秒
        factory.setConnectTimeout(5000); //读取超时时间,单位毫秒
        RestTemplate restTemplate = new RestTemplate(factory); //根据工厂类的配置创建RestTemplate对象
        //设置编码格式,getMessageConverters()方法会得到一个属性集合,用新的编码转换器替换掉原编码转换器(第一个属性)
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
