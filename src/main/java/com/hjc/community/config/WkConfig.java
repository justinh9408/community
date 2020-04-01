package com.hjc.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @Classname WkConfig
 * @Description TODO
 * @Date 2020-03-30 3:10 p.m.
 * @Created by Justin
 */
@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @PostConstruct
    public void init() {
        // 创建WK图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()) {
            file.mkdir();
            logger.info("创建WK图片目录: " + wkImageStorage);
        }
        logger.info("创建WK图片目录检查完毕: " + wkImageStorage);

    }

    public static void main(String[] args) {
        String path = "/Users/Justin/Desktop/work/data/wkimage";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("chuang jian ");
        }
        System.out.println("done");
    }
}
