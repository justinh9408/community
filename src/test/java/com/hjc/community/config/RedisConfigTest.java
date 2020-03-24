package com.hjc.community.config;

import com.hjc.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;


/**
 * @Classname RedisConfigTest
 * @Description TODO
 * @Date 2020-03-04 12:00 p.m.
 * @Created by Justin
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class RedisConfigTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("count","1");
        System.out.println(redisTemplate.opsForValue().get("count"));
    }
    @Test
    public void testBound() {
        String key = "count";
        BoundValueOperations ops = redisTemplate.boundValueOps(key);
        System.out.println(ops.get());
        ops.increment();
        ops.increment();
        ops.increment();
        ops.increment();
        ops.increment();
        System.out.println(ops.get());
    }
}