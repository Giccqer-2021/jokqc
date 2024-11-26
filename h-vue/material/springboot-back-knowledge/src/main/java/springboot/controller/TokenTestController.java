package springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.dto.MessageDto;

/**
 * 测试该页面是否能正常验证token,授予token.
 */
@RestController
@RequestMapping("/token")
public class TokenTestController {
    /**
     * 该页面只有在被拦截器检验通过后访问到,需要用户拥有正确的token.
     *
     * @return 如果验证通过,那么前端会收到的信息.
     */
    @GetMapping("/blocked_page")
    public MessageDto blockedPage() {
        return new MessageDto("皮皮龙", "你发现了我的秘密哈哈哈哈");
    }

    /**
     * 访问该页面将会授予前端一个正确的token.
     *
     * @return 授予前端的token.
     */
    @GetMapping("/get_token")
    public MessageDto getToken() {
        return new MessageDto("token", "rnfmabj");
    }

    /**
     * 访问该页面会授予前端一个错误的token,携带该token的请求将无法通过拦截器的验证.
     *
     * @return 授予前端的错误的token.
     */
    @GetMapping("/get_error_token")
    public MessageDto getErrorToken() {
        return new MessageDto("token", "Angela");
    }
}
