package com.hjc.community.controller;

import com.google.code.kaptcha.Producer;
import com.hjc.community.entity.User;
import com.hjc.community.service.UserService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Classname LoggingController
 * @Description TODO
 * @Date 2020-02-19 6:23 p.m.
 * @Created by Justin
 */
@Controller
public class LoggingController implements CommunityConstant {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    UserService userService;

    @Autowired
    Producer kaptchaProducer;

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/register")
    public String toRegisterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String toLoginPage() {
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        try {
            Map<String, String> map = userService.register(user);
            if (map == null || map.isEmpty()) {
                model.addAttribute("msg", "注册成功，请查收邮件激活");
                model.addAttribute("target", "/index");
                return "/site/operate-result";
            } else {
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
//        http://localhost:8080/community/activation/101/code

    @GetMapping("/activation/{userId}/{code}")
    public String activate(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activateAcc(userId, code);
        if (result == ACTIVATE_SUCCESS) {
            model.addAttribute("msg", "成功激活");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATE_REPEAT) {
            model.addAttribute("msg", "改账号已被激活激活");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void kaptchaImg(HttpServletResponse response/*, HttpSession session*/) {
//        生成验证码
        String kaptchaText = kaptchaProducer.createText();
//        存入session
//        session.setAttribute("kaptcha", kaptchaText);
//        存入redis
        String kapOwner = CommunityUtil.generateUUID();
        String kaptchaKey = RedisUtil.getKaptchaKey(kapOwner);
        redisTemplate.opsForValue().set(kaptchaKey, kaptchaText, 60, TimeUnit.SECONDS);
//        owner存入Cookie
        Cookie cookie = new Cookie("kaptchaOwner", kapOwner);
        cookie.setPath(contextPath);
        cookie.setMaxAge(60);
        response.addCookie(cookie);
//        生成图片
        BufferedImage image = kaptchaProducer.createImage(kaptchaText);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
//        String  kaptcha = (String)session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String kaptchaKey = RedisUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(kaptchaKey);
        }
        if (StringUtils.isBlank(code) || StringUtils.isBlank(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", (String) map.get("ticket"));
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/index";
    }
}
