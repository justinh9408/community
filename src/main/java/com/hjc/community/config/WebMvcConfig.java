package com.hjc.community.config;

import com.hjc.community.controller.Intercepter.LoginRequiredIntercepter;
import com.hjc.community.controller.Intercepter.LoginTicketIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Classname WebMvcConfig
 * @Description TODO
 * @Date 2020-02-20 7:07 p.m.
 * @Created by Justin
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginTicketIntercepter loginTicketIntercepter;
    @Autowired
    LoginRequiredIntercepter loginRequiredIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketIntercepter)
                .excludePathPatterns("/**/*.css", "/**/*.js","/**/*.jpeg","/**/*.png","/**/*.jpg");

        registry.addInterceptor(loginRequiredIntercepter)
                .excludePathPatterns("/**/*.css", "/**/*.js","/**/*.jpeg","/**/*.png","/**/*.jpg");
    }
}
