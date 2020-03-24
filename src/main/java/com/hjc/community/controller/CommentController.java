package com.hjc.community.controller;

import com.hjc.community.entity.Comment;
import com.hjc.community.entity.DiscussPost;
import com.hjc.community.entity.Event;
import com.hjc.community.event.EventProducer;
import com.hjc.community.service.CommentService;
import com.hjc.community.service.DiscussPostService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Classname CommentController
 * @Description TODO
 * @Date 2020-02-26 4:56 p.m.
 * @Created by Justin
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    DiscussPostService postService;


    @RequestMapping("/add/{postId}")
    public String addComment(@PathVariable("postId") int postId, Comment comment) {
        int userId = hostHolder.getUser().getId();
        comment.setUserId(userId);
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", postId);
        //查找通知对象id
        if (comment.getEntityType() == COMMENT_TYPE_POST) {
            DiscussPost target = postService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserid(target.getUserId());
        } else if (comment.getEntityType() == COMMENT_TYPE_REPLY) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserid(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == COMMENT_TYPE_POST) {
            // 触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(COMMENT_TYPE_POST)
                    .setEntityId(postId);
            eventProducer.fireEvent(event);
        }
        return "redirect:/discuss/detail/" + postId;
    }
}
