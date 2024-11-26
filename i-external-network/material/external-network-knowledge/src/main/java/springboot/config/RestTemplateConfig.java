package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * RestTemplate配置类.
 * <P>{@code RestTemplate} 对象用于后端主动发送http请求,主动上传下载等</P>
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory(); //创建请求工厂
        factory.setReadTimeout(5000); //设置读取超时时间,单位毫秒
        factory.setConnectTimeout(5000); //设置连接超时时间,单位毫秒
        RestTemplate restTemplate = new RestTemplate(factory); //使用工厂对象创建restTemplate
        //设置对象的编码格式
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
