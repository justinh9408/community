package com.hjc.community.controller;

import com.hjc.community.entity.Event;
import com.hjc.community.entity.User;
import com.hjc.community.event.EventProducer;
import com.hjc.community.service.LikeService;
import com.hjc.community.service.ScoreService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname LikeController
 * @Description TODO
 * @Date 2020-03-04 6:37 p.m.
 * @Created by Justin
 */
@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    ScoreService scoreService;

    @PostMapping("like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityAuthorId, int postId) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId, entityAuthorId);
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        int likeStatus = likeService.findLikeStatus(user.getId(), entityType, entityId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        if (likeStatus == 1) {
//            触发点赞事件
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(user.getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserid(entityAuthorId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);
        }
        if (entityType == COMMENT_TYPE_POST) {
            scoreService.scoreChangedPosts(entityId);
        }


        return CommunityUtil.getJsonObject(0, null, map);
    }
}
