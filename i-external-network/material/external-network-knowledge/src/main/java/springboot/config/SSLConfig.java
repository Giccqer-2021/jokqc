package springboot.config;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用来配置开放额外的端口,或是设置当访问http协议时跳转至https协议.
 * <p>本方法也可以用来为服务器配置多张ssl证书</p>
 */
@Configuration
public class SSLConfig {
    /**
     * 配置http协议跳转至https协议.
     * <p>方法内的注释大部分由通义灵码生成</p>
     *
     * @return 服务器工厂配置对象
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatServerCustomer() {
        return (factory) -> {
            factory.addContextCustomizers((context) -> { //添加安全约束
                SecurityConstraint constraint = new SecurityConstraint(); //创建安全约束对象
                constraint.setUserConstraint("CONFIDENTIAL"); //设置安全约束级别
                SecurityCollection collection = new SecurityCollection(); //创建安全集合对象
                collection.addPattern("/"); //设置要保护的路径
                constraint.addCollection(collection); //将安全集合对象添加到安全约束对象中
                context.addConstraint(constraint); //将安全约束对象添加到服务器工厂中
            });//注:如果仅仅是增加新的端口,则不必添加factory.addContextCustomizers()方法
            Connector connector = new Connector();//创建一个Connector,默认使用HTTP/1.1协议
            connector.setPort(80); //设置原端口号
            connector.setScheme("http"); //设置协议
            connector.setSecure(true);// 这里暂时关闭了跳转,若想打开跳转则使用下面的语句
//            connector.setSecure(false); //该端口是否为安全端口,若不是则进行跳转
            connector.setRedirectPort(443); //设置重定向的端口号
            factory.addAdditionalTomcatConnectors(connector); //将自定义的Connector添加到工厂中
        };
    }
}
