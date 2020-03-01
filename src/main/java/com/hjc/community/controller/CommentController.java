package com.hjc.community.controller;

import com.hjc.community.entity.Comment;
import com.hjc.community.service.CommentService;
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
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping("/add/{postId}")
    public String addComment(@PathVariable("postId") int postId, Comment comment) {
        int userId = hostHolder.getUser().getId();
        comment.setUserId(userId);
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return "redirect:/discuss/detail/" + postId;
    }
}
