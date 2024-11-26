package springboot;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

/**
 * 本类用于springboot发送邮件测试.
 */
@SpringBootTest
public class SendEmailTest {
    /**
     * 默认的邮件发送对象.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * 邮件发送者账号.
     * <p>使用 {@code @Value()} 注解从配置文件中注入字符串值</p>
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送纯文本简单邮件.
     */
    @Test
    public void sendSimpleEmail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage(); // 创建简单邮件对象
        simpleMailMessage.setFrom(username); // 发送者
        simpleMailMessage.setTo("1293711097@qq.com"); // 接收者
        simpleMailMessage.setSubject("你好,我是springboot"); // 邮件标题
        simpleMailMessage.setText("这是一封来自springboot工程的测试用简单邮件."); // 邮件内容
        javaMailSender.send(simpleMailMessage); // 发送邮件
    }

    /**
     * 发送附有文件的邮件.
     * <p>也太慢了吧</p>
     */
    @Test
    public void sendFileByEmail() {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage(); // 创建mine(多用途互联网邮件扩展)格式邮件对象
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true); // 创建邮件帮助对象
            helper.setFrom(username);
            helper.setTo("1293711097@qq.com");
            helper.setSubject("你好,我是springboot");
            helper.setText("附赠好听的歌曲一首");
            File sendFile = new File("download\\ChipiChipiChapaChapa.ogg"); // 要发送的文件
            helper.addAttachment(sendFile.getName(),sendFile); // 添加附件,如果有多个附件可以多次添加
            javaMailSender.send(mimeMailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
