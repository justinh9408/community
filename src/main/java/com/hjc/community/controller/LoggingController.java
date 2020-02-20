package com.hjc.community.controller;

import com.hjc.community.entity.User;
import com.hjc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @Classname LoggingController
 * @Description TODO
 * @Date 2020-02-19 6:23 p.m.
 * @Created by Justin
 */
@Controller
public class LoggingController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String toRegisterPage() {
        return "/site/register";
    }

    @PostMapping("/register")
    public String register(Model model,User user) {
        try {
            Map<String, String> map = userService.register(user);
            if (map == null || map.isEmpty()) {
                model.addAttribute("msg","注册成功，请查收邮件激活");
                model.addAttribute("target", "/index");
                return "/site/operate-result";
            }else{
                model.addAttribute("usernameMsg", map.get("usernameMsg"));
                model.addAttribute("passwordMsg", map.get("passwordMsg"));
                model.addAttribute("emailMsg", map.get("emailMsg"));
                return "/site/register";
            }
        } catch (IllegalAccessException e) {
            model.addAttribute("msg", "系统错误");
            System.out.println("系统错误");
            e.printStackTrace();
            return "/site/register";
        }
    }
}
