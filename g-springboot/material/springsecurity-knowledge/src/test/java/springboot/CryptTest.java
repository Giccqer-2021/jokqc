package springboot;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 用以测试SpringSecurity的加密方法Crypt算法的测试类.
 * <p>用来生成一些加密后的密码样例,测试时不需要启动整个服务器</p>
 * <p>使用 {@link BCryptPasswordEncoder} 的 {@code encode} 方法进行加密时,会生成一个包含盐值的加密哈希值,无需手动加盐</p>
 * <p>由于盐值是随机生成的,每次加密后的内容皆不相同,但都能与明文密码匹配</p>
 */
public class CryptTest {
    @Test
    public void printTest() {
        String password = "123456";//原始密码,最好别定义成这样
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();//加密器
        String encode1 = bCryptPasswordEncoder.encode(password);//第一次加密
        System.out.println("第一次加密后的密码: " + encode1);
        String encode2 = bCryptPasswordEncoder.encode(password);//第二次加密,与上一次生成的密码不同
        System.out.println("第二次加密后的密码: " + encode2);
        //验证密码是否匹配,输出 true
        System.out.println("第一次加密后的密码解密: " + bCryptPasswordEncoder.matches(password, encode1));
        //验证第二次加密后的密码是否匹配,输出 true ,即使加密后生成的密码不同也可被匹配
        System.out.println("第二次加密后的密码解密: " + bCryptPasswordEncoder.matches(password, encode2));
    }
}
