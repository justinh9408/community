package com.hjc.community.util;

import com.hjc.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Classname SensitivityFilterTest
 * @Description TODO
 * @Date 2020-02-21 3:52 p.m.
 * @Created by Justin
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class SensitivityFilterTest {
    @Autowired
    SensitivityFilter sensitivityFilter;

    @Test
    public void testFilter() {
        String text = "赌博啦啦啦吸毒流量计离开";
        String filter = sensitivityFilter.filter(text);
        System.out.println(filter);


    }
}