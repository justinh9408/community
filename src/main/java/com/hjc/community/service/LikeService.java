package com.hjc.community.service;

import com.hjc.community.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Classname LikeService
 * @Description TODO
 * @Date 2020-03-04 6:30 p.m.
 * @Created by Justin
 */
@Service
public class LikeService {

    @Autowired
    RedisTemplate redisTemplate;

    //    点赞
    public void like(int userId, int entityType, int entityId, int entityAuthorId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
                String entityUserLikeKey = RedisUtil.getUserLikeKey(entityAuthorId);
                Boolean isMember = operations.opsForSet().isMember(entityKey, userId);
                operations.multi();
                if (isMember) {
                    operations.opsForSet().remove(entityKey, userId);
                    operations.opsForValue().decrement(entityUserLikeKey);
                } else {
                    operations.opsForSet().add(entityKey, userId);
                    operations.opsForValue().increment(entityUserLikeKey);
                }

                return operations.exec();
            }
        });
    }

    //  查询某实体点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityKey);

    }

    //  查询是否已点赞
    public int findLikeStatus(int userId, int entityType, int entityId) {
        String entityKey = RedisUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityKey, userId) ? 1 : 0;
    }

    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }


}
