package com.relaxed.test.mail;

import cn.hutool.core.lang.Assert;
import com.relaxed.common.job.XxlJobAutoConfiguration;
import com.relaxed.common.job.properties.XxlJobProperties;
import com.relaxed.common.mail.MailAutoConfiguration;
import com.relaxed.extend.mail.model.MailSendInfo;
import com.relaxed.extend.mail.sender.MailSender;
import com.relaxed.test.job.LogXxlJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Yakir
 * @Topic MailTest
 * @Description
 * @date 2024/12/23 14:25
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = {MailSenderAutoConfiguration.class,MailAutoConfiguration.class},properties = "spring.config.location=classpath:/mail/application-mail.yml")
public class MailTest {

    @Autowired
    private MailSender mailSender;
    @Test
    public void mailSend(){
        MailSendInfo mailSendInfo = mailSender.sendTextMail("测试", "测试内容", "yushuo@vipsave.cn");
        log.info("发送结果:{}",mailSendInfo.getSuccess());
        Assert.isTrue(mailSendInfo.getSuccess(),"邮件发送失败,错误消息:{}",mailSendInfo.getErrorMsg());

    }
}
