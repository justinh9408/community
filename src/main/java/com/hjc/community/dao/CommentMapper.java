package com.hjc.community.dao;

import com.hjc.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname CommentMapper
 * @Description TODO
 * @Date 2020-02-26 12:45 p.m.
 * @Created by Justin
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}
