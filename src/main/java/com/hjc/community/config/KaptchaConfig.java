package com.hjc.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Classname KaptchaConfig
 * @Description TODO
 * @Date 2020-02-20 1:21 p.m.
 * @Created by Justin
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","black");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789abcdefghijklmnopqrstuvwxyz");
        properties.setProperty("kaptcha.textproducer.char.length","4");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;

    }
}
