package com.hjc.community.service;

import com.hjc.community.dao.UserMapper;
import com.hjc.community.entity.User;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2020-02-18 4:24 p.m.
 * @Created by Justin
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MailClient mailClient;

    @Autowired
    TemplateEngine templateEngine;

    @Value("@{community.path.domain}")
    String domain;

    @Value("${server.servlet.context-path}")
    String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, String> register(User user) throws IllegalAccessException {
        HashMap<String, String> map = new HashMap<>();

        if (user == null) {
            throw new IllegalAccessException("用户不能为空");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮件不能为空");
            return map;
        }

        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "用户名已存在");
            return map;
        }

        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "邮件已存在");
            return map;
        }

        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

//        发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
//        http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendEMail(user.getEmail(), "Registration Activation", content);

        return map;
    }
}
