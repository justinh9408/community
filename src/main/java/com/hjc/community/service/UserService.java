package com.hjc.community.service;

import com.hjc.community.dao.UserMapper;
import com.hjc.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname UserService
 * @Description TODO
 * @Date 2020-02-18 4:24 p.m.
 * @Created by Justin
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
