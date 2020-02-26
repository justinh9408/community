package com.hjc.community.util;

import com.hjc.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Classname HostHolder
 * @Description TODO
 * @Date 2020-02-20 6:40 p.m.
 * @Created by Justin
 */
@Component
public class HostHolder {
    ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
