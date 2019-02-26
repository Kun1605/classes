package cn.kunakun.controller.admin;

import cn.kunakun.utils.ThreadPoolUtil;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private JavaMailSender mailSender;


    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);

    /**
     * 监控solr程序挂了之后,给劳资邮件
     *
     * @Author YangKun
     * @Date 2018/10/30
     */
    @RequestMapping("/solr")
    @ResponseBody
    public Object solr() {
            ThreadPoolUtil.executorThreadPool(()->{
                MimeMessage mimeMessage = this.mailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                try {
                    message.setFrom("1321366271@qq.com");//设置发信人，发信人需要和spring.mail.username配置的一样否则报错
                    message.setTo("1321366271@qq.com");				//设置收信人
                    message.setSubject("警告,服务停止了!!!");	//设置主题
                    message.setText("<h1>不好意思,你的<em color='red'>solr</em>服务停止了,抓紧去看下吧</h1>",true);  	//第二个参数true表示使用HTML语言来编写邮件
                    logger.debug("已经发送了邮件");
                    this.mailSender.send(mimeMessage);
                } catch (Exception e) {
                    logger.debug(Throwables.getStackTraceAsString(e));
                }
            });
        return "已经发送通知了杨坤!";
    }

}
