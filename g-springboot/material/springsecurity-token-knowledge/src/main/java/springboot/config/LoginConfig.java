package springboot.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.entity.UserEntity;
import springboot.message.Notification;
import springboot.message.UserInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 配置使用 jwt token 进行登陆状态验证,并修改了相关登陆页面的配置类.
 * <p>实际环境中请适当解耦这些代码,减少长度,比如:将用户信息类转化为json数据并封装入jwt载荷中的过程可在util工具类的静态方法中实现</p>
 * <p>同理,处理页面登录成功,失败或是过滤器类建议单独书写对应的实现类</p>
 */
@Configuration
public class LoginConfig {
    /**
     * 秘钥, 用于生成token的盐值,绝不可以泄露.
     */
    private static final String SECRET = "3Gdr0/h8Dd03=e3dAU";

    /**
     * redis的字符串数据处理与传输模板对象.
     * <p>此模板自带字符串序列化方式,无需手动配置</p>
     */
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 配置登陆验证.
     * <p>基本上属于固定写法,照写便是.与后端渲染不同,这里配置的"页面地址"不再从后端返回一个页面,而是返回一条json封装的消息</p>
     *
     * @param httpSecurity 一个用于创建过滤器链对象的工具类
     * @return 创建的过滤器链对象
     * @throws Exception 什么样的异常都可能有,比如json转换异常,io异常等
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(fromLogin -> fromLogin
                        /*自定义登陆页面,已失效,由于关闭了session验证方式所以所以在过滤器验证token合法后,所有非登陆类型的请求
                        (或非post类型的登录请求)都会被默认"需要"使用账号密码再次登录才能访问,这里配置自定义的"登陆页面"以阻止后
                        端发送默认的登陆页面再次验证登录,自定义"登录页面"只需发送文本消息即可
                        注意:当发生 404,405 错误时,客户端页面都会直接跳转到这里*/
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/login") //提交请求的地址,固定
                        .defaultSuccessUrl("/") //登陆成功(账号密码输入正确)后跳转的"页面"
                        .failureUrl("/loginFailed") //登陆失败(账号密码错误)后跳转的"页面"
                        // 成功登陆后,调用此方法,将token写入redis,并返回给前端
                        .successHandler((request, response, authentication) -> {
                            // 获取登陆时的认证信息,获得的实体类类型取决于在Service层的相关配置
                            UserEntity user = (UserEntity) authentication.getPrincipal();
                            UserInfo userInfo = new UserInfo(user);//使用相关信息类重新封装上述登陆信息,避免密码等敏感信息被写入jwt载荷
                            String userJSON = JSONUtil.toJsonStr(userInfo);//将登陆信息类封装为json字符串
                            //生成token,需提供载荷名和加密秘钥
                            String token = JWTUtil.createToken(Map.of("user", userJSON), SECRET.getBytes());
                            redisTemplate.opsForValue().set("t" + user.getId(), token); //写入redis
                            writeJSONMessage(response, token);//自定义的方法,将token写入响应体中返回
                        })
                        .failureHandler((request, response, exception) -> //登陆失败后,调用此方法,返回错误消息
                                writeJSONMessage(response, exception.getMessage())))
                .csrf(AbstractHttpConfigurer::disable) //关闭csrf验证,使用token本身可以避免csrf攻击
                .sessionManagement(sessionManagement ->//取消默认的session验证策略,改为无状态验证
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->//除了"登陆"页面,所有的页面都需要拦截,后端不再提供静态资源的访问
                        request.requestMatchers("/loginPage").permitAll().anyRequest().authenticated())
                // 自定义过滤器,用于验证token合法性,并设置认证信息.过滤器会在SpringSecurity默认方法进行验证拦截前执行
                .addFilterAfter(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                        //获取请求头中的Authorization属性字段(即token),该字段已由前端配置完毕
                        String token = request.getHeader("Authorization");
                        if (request.getRequestURI().equals("login")) { //如果是登陆请求,由于尚未生成token,直接放行
                            filterChain.doFilter(request, response);//向下传透放行
                        } else if (!StringUtils.hasText(token)) { //如果请求头中没有token字段,直接返回错误消息
                            writeJSONMessage(response, "请求token为空");
                        } else if (!JWTUtil.verify(token, SECRET.getBytes())) { //如果token验签未通过,直接返回错误消息
                            writeJSONMessage(response, "请求token非法");
                        } else {
                            JWT jwt = JWTUtil.parseToken(token); //解析token
                            String userJSON = jwt.getPayload("user").toString(); //获取token载荷中的用户信息类
                            UserInfo userInfo = JSONUtil.toBean(userJSON, UserInfo.class); //将用户信息转换为实体类对象
                            //根据用户实体类对象中的id,查询redis数据库,比较token是否一致,若不一致则不通过
                            if (!token.equals(redisTemplate.opsForValue().get("t" + userInfo.getId()))) {
                                writeJSONMessage(response, "请求token未通过");
                            } else { //通过验证,设置认证信息
                                //在SpringSecurity的上下文中放入认证信息，表示该用户曾经登陆过,否则每次访问后端请求都会索要账号和密码
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken); //认证信息放入上下文
                                filterChain.doFilter(request, response); //向下传透放行
                            }
                        }
                    }
                }, UsernamePasswordAuthenticationFilter.class)
                //当发生 403 权限不足的错误时的处理办法,后端不再提供相应的页面,要自行传递消息配置
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        writeJSONMessage(response, "访问权限不足");
                    });
                });
        return httpSecurity.build();
    }

    /**
     * 自定义的将信息封装入消息类并写入响应体的方法.
     * <p>如有需要,可将其写入 util 工具类中以静态方法提供</p>
     *
     * @param response 即将返回的响应体
     * @param message  要返回的消息
     */
    private void writeJSONMessage(HttpServletResponse response, String message) {
        response.setContentType("application/json");//声明返回类型为json
        response.setCharacterEncoding("utf-8");//设置编码,避免中文乱码
        String json = JSONUtil.toJsonStr(new Notification(message));//Notification为自定义的简单消息传递类
        try (PrintWriter printWriter = response.getWriter()) {//其实不必写这么复杂,这只是种优雅的写法
            printWriter.write(json);
            printWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("json转换异常");
        }
    }
}
