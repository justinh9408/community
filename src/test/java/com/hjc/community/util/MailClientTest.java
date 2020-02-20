package com.hjc.community.util;

import com.hjc.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname MailClientTest
 * @Description TODO
 * @Date 2020-02-19 11:11 a.m.
 * @Created by Justin
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class MailClientTest {
    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void sendMailTest() {
        mailClient.sendEMail("445056401@qq.com", "Welcom", "Welcom HJC");
    }

    @Test
    public void sendHtmlMailTest() {
        Context context = new Context();
        context.setVariable("username","hjc");

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendEMail("445056401@qq.com", "html mail", content);

    }
}