package com.hjc.community;

import com.hjc.community.dao.UserMapper;
import com.hjc.community.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Classname MapperTests
 * @Description TODO
 * @Date 2020-02-18 1:44 p.m.
 * @Created by Justin
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    private static final Logger logger = LoggerFactory.getLogger(MapperTests.class);

    @Autowired
    UserMapper userMapper;


    @Test
    public void testUserMapper() {
        assert userMapper != null;
        User user = userMapper.selectById(101);
        System.out.println(user);

    }

    @Test
    public void testLogger() {
        System.out.println(logger.getName());

        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");
    }
}
