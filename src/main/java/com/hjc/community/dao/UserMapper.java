package com.hjc.community.dao;

import com.hjc.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2020-02-18 1:41 p.m.
 * @Created by Justin
 */
@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
