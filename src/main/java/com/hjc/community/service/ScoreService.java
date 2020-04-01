package com.hjc.community.service;

import com.hjc.community.entity.DiscussPost;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Classname ScoreService
 * @Description TODO
 * @Date 2020-03-29 2:21 p.m.
 * @Created by Justin
 */
@Service
public class ScoreService implements CommunityConstant {



    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    LikeService likeService;

    @Autowired
    ElasticSearchService elasticSearchService;

    private static final Date epoch;

    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败!", e);
        }
    }

    public void scoreChangedPosts(int postId) {
        String postScoreKey = RedisUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(postScoreKey, postId);
    }

    public double calculateScore(DiscussPost post) {
        if (post == null) {
            return 0.0;
        }

        // 是否精华
        boolean wonderful = post.getStatus() == 1;
        // 评论数量
        int commentCount = post.getCommentCount();
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(COMMENT_TYPE_POST, post.getId());

        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数 = 帖子权重 + 距离天数
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);

        return score;

    }

    public void refresh(Integer postId) {
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if (post == null) {
            return;
        }
        double score = calculateScore(post);

        // 更新帖子分数
        discussPostService.updateScore(postId, score);
        // 同步搜索数据
        post.setScore(score);
        elasticSearchService.saveDiscussPost(post);
    }
}
