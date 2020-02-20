package com.hjc.community.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Classname MailClient
 * @Description TODO
 * @Date 2020-02-19 11:05 a.m.
 * @Created by Justin
 */
@Component
public class MailClient {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String from;

    public void sendEMail(String to, String subj, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(from);
            System.out.println(from);
            helper.setTo(to);
            helper.setText(content, true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
