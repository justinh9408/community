package com.hjc.community.controller;

import com.hjc.community.Annotation.LoginRequired;
import com.hjc.community.entity.User;
import com.hjc.community.service.FollowService;
import com.hjc.community.service.LikeService;
import com.hjc.community.service.UserService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2020-02-20 9:30 p.m.
 * @Created by Justin
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @LoginRequired
    @GetMapping("/setting")
    public String toSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/updateHeader")
    public String updateHeaderUrl(Model model, MultipartFile multipartFile) {
        if (multipartFile == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String filename = multipartFile.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确!");
            return "/site/setting";
        }

        filename = CommunityUtil.generateUUID() + suffix;
        File dest = new File(uploadPath + "/" + filename);
        try {
            multipartFile.transferTo(dest);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }


    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

    @LoginRequired
    @PostMapping("/updatePwd")
    public String changePwd(Model model, String oldPwd, String newPwd) {
        User user = hostHolder.getUser();
        String oldPwdEncrypt = CommunityUtil.md5(oldPwd + user.getSalt());
        if (!oldPwdEncrypt.equals(user.getPassword())){
            model.addAttribute("oldPwdError","原密码不正确");
            return "/site/setting";
        }
        userService.updatePassword(user.getId(), newPwd);
        return "redirect:/index";
    }

    @GetMapping("/profile/{userId}")
    public String profilePage(Model model, @PathVariable("userId") int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException(("找不到用户"));
        }
        model.addAttribute("user", user);

        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

//        关注数量
        Long followeeCount = followService.findFolloweeCount(userId, TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
//        粉丝数量
        Long followerCount = followService.findFollowerCount(TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        boolean hasFollowed = false;
//        是否关注
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.isFollowed(hostHolder.getUser().getId(), TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);
        
        return "site/profile";
    }
}
