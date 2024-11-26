package springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.annotation.CustomAnnotation;
import springboot.dto.DataFormatDto;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 进行初步测试的视图层.
 * <p>使用 {@code @Controller} 注解标记</p>
 */
@Controller
public class DefaultController {
    /**
     * 服务器端口号.
     * <p> @Value() 表示注入 application.yml 文件中的某值</p>
     */
    @Value("${server.port}")
    private int port;

    /**
     * 第一个视图层页面.
     * <p>@{@link CustomAnnotation} 为自定义注解,用以测试aop切面编程配置是否对该注解生效</p>
     *
     * @return 返回的响应体内容
     */
    @GetMapping("/hello")
    @CustomAnnotation(">>>这是一个自定义的注解<<<")
    public @ResponseBody String helloPage() {
        System.out.println("------已输出Hello页面------");
        return "<p>你好,中国</p>";
//        return "<p>这个标签用来测试热部署依赖</p>";
    }

    /**
     * 对使用thymeleaf作为引擎模版的SpringBoot网页视图层技术进行初步测试.
     * <p>请导入相关依赖,并配置文件配置好thymeleaf模板引擎所映射的模板位置的前缀和后缀</p>
     *
     * @param model 模板引擎所使用的Model
     * @return thymeleaf视图页面的相对路径
     */
    @GetMapping("/thymeleaf/hello")
    public String thymeleafPage(Model model) {
        model.addAttribute("CustomInfo", "这是自定义的属性");// 向模板引擎传递数据
        return "hello";// /templates/hello.html
    }

    /**
     * 测试日期能否正确使用json格式化.
     * <p>如果没有进行json格式配置,则除了最后两个对象外其余对象的时间输出到网页时会提前八小时,甚至日期也会因此受到影响</p>
     *
     * @return DataFormatDto封装的json数据
     * @throws ParseException 日期格式化异常
     */
    @GetMapping("/get-date")
    @ResponseBody
    public DataFormatDto getDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        Date utilDate = sdf.parse("2024年10月1日 6时00分00秒");
        Timestamp sqlTimestamp = new Timestamp(utilDate.getTime());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); //仅能输出日期但不输出时间
        Time sqlTime = new Time(utilDate.getTime()); //仅能输出时间但不输出日期
        return new DataFormatDto(utilDate, sqlTimestamp, sqlDate, sqlTime);
    }

    /**
     * 本方法用于在网页中输出服务器所用的端口号.
     *
     * @return 显示在网页中的结果
     */
    @GetMapping("/get-port")
    public @ResponseBody String getPort() {
        return "<p>当前服务器端口号为:" + this.port + "</p>";
    }
}
