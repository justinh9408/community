package com.hjc.community.service;

import com.hjc.community.dao.CommentMapper;
import com.hjc.community.entity.Comment;
import com.hjc.community.entity.DiscussPost;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.SensitivityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Classname CommentService
 * @Description TODO
 * @Date 2020-02-26 12:51 p.m.
 * @Created by Justin
 */
@Service
public class CommentService implements CommunityConstant {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    SensitivityFilter sensitivityFilter;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("不能为空");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitivityFilter.filter(comment.getContent()));
        int i = commentMapper.insertComment(comment);

        if (comment.getEntityType() == COMMENT_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(COMMENT_TYPE_POST, comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        return i;
    }

    public Comment findCommentById(int id) {
        return commentMapper.findCommentById(id);
    }

}
