package com.hjc.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname Event
 * @Description TODO
 * @Date 2020-03-17 11:48 a.m.
 * @Created by Justin
 */
public class Event {

    private String topic;
    private int userId;
//    事件发生作用于的实体
    private int entityType;
    private int entityId;
    private int entityUserid;
    private Map<String, Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserid() {
        return entityUserid;
    }

    public Event setEntityUserid(int entityUserid) {
        this.entityUserid = entityUserid;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
