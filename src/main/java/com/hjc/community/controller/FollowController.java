package com.hjc.community.controller;

import com.hjc.community.entity.Event;
import com.hjc.community.entity.Page;
import com.hjc.community.entity.User;
import com.hjc.community.event.EventProducer;
import com.hjc.community.service.FollowService;
import com.hjc.community.service.UserService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Classname FollowController
 * @Description TODO
 * @Date 2020-03-06 1:21 p.m.
 * @Created by Justin
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @PostMapping("/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

//        触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(user.getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserid(entityId);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJsonObject(0, "关注成功！");
    }

    @PostMapping("/unFollow")
    @ResponseBody
    public String unFollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        followService.unFollow(user.getId(), entityType, entityId);
        return CommunityUtil.getJsonObject(0, "已取消关注！");
    }

    @GetMapping("/followees/{userId}")
    public String followeesList(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows(followService.findFolloweeCount(userId, TYPE_USER).intValue());

        List<Map<String, Object>> userList = followService.followeesList(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows(followService.findFollowerCount(TYPE_USER, userId).intValue());

        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }

        return followService.isFollowed(hostHolder.getUser().getId(), TYPE_USER, userId);
    }

}
