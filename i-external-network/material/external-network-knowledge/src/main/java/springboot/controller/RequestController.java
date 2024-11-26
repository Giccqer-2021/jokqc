package springboot.controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * 后端主动发送http请求的控制器.
 * <p>前两个使用springboot原生方法发送get,post请求,后者使用hutool工具包</p>
 */
@Controller
@RequestMapping("/request")
public class RequestController {
    /**
     * http请求发送器,已在配置文件中声明.
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 先让后端主动发送请求获取百度页面,然后再把页面发送给后端.
     * <p>如果使用默认的 {@code RestTemplate} 请求发送器则会产生乱码的问题,因此需要对该发送器进行配置修改</p>
     *
     * @return
     */
    @GetMapping("/baidu")
    @ResponseBody
    public String getBaiduPage() {
        String url = "https://www.baidu.com/";
        return restTemplate.getForObject(url, String.class); //返回由纯字符串组成的html页面
    }

    /**
     * 后端主动发送post请求.
     * <p><a href="https://echo.apifox.com">apifox网站</a> 是用于进行接口测试的公共网站,可以用于检测你的请求方法和发送的参数是否正确</p>
     *
     * @return 返回的的响应体
     */
    @GetMapping("/apifox")
    public ResponseEntity<?> postToApifox() {
        String url = "https://echo.apifox.com/post";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); //请求体内的参数集合
        params.add("userName", "堂吉诃德");
        params.add("userPassword", "123456");
        HttpHeaders headers = new HttpHeaders(); //请求头,注意不要主动将Content-Type设置为application/json否则发出的参数会多出一对引号
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers); //封装请求体和请求头
        return restTemplate.postForEntity(url, httpEntity, String.class); //发送请求并返回响应体
    }

    /**
     * 使用hutool工具包发送get请求.
     * <p>获得返回的响应体后,获取其中响应体内的字符串数据,用自定义的springboot原生响应体对象重新封装后再返回给前端</p>
     *
     * @return 自定义的响应体
     */
    @GetMapping("/apifox2")
    public ResponseEntity<String> hutoolGet() {
        String url = "https://echo.apifox.com/get?param1=以实玛利&param2=都是你的错";
        String responseBodyStr = HttpRequest.get(url) // 发送get请求
                .header(Header.CONTENT_TYPE, "application/json") // 设置请求头
                .timeout(20000) // 设置超时时间
                .execute().body(); // 执行get方法,获取响应体内的字符串数据
        MultiValueMap<String, String> responseBody = new LinkedMultiValueMap<>(); //自定义响应体,封装获取到的字符串数据
        responseBody.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(responseBodyStr, responseBody, HttpStatus.OK);
    }

    /**
     * 使用hutool工具包发送post请求.
     * <p>同理,只不过使用 {@code formStr()} 方法封装了请求体的数据</p>
     *
     * @return
     */
    @GetMapping("/apifox3")
    public ResponseEntity<String> hutoolPost() {
        String url = "https://echo.apifox.com/post";
        HashMap<String, String> paramsMap = new HashMap<>(); // 请求体参数
        paramsMap.put("userName", "格里高尔");
        paramsMap.put("userPassword", "654321");
        String responseBodyStr = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json")
                .formStr(paramsMap) // 使用formStr()方法封装请求体
                .timeout(20000)
                .execute().body();
        MultiValueMap<String, String> responseBody = new LinkedMultiValueMap<>();
        responseBody.add("Content-Type", "application/json;charset=UTF-8");
        return new ResponseEntity<>(responseBodyStr, responseBody, HttpStatus.OK);
    }
}
