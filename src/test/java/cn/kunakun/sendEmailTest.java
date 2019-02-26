package cn.kunakun;

import org.jsoup.Jsoup;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
public class sendEmailTest {
    @Autowired
    private JavaMailSender mailSender;


    @org.junit.Test
    public void sendEmail() {
        try {
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("1321366271@qq.com");//设置发信人，发信人需要和spring.mail.username配置的一样否则报错
            message.setTo("1321366271@qq.com");				//设置收信人
            message.setSubject("测试邮件主题");	//设置主题
            message.setText("杨坤",true);  	//第二个参数true表示使用HTML语言来编写邮件
            this.mailSender.send(mimeMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
