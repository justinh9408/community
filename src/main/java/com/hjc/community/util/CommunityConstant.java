package com.hjc.community.util;

/**
 * @Classname ActivationCode
 * @Description TODO
 * @Date 2020-02-20 1:01 a.m.
 * @Created by Justin
 */
public interface CommunityConstant {
    int ACTIVATE_SUCCESS = 0;

    int ACTIVATE_REPEAT = 1;

    int ACTIVATE_FAIL = 2;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 12 * 100;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    int COMMENT_TYPE_POST = 1;

    int COMMENT_TYPE_REPLY = 2;

    int TYPE_USER = 3;

    int SYSTEM_USER_ID = 1;

    String TOPIC_COMMENT = "COMMENT";

    String TOPIC_LIKE = "LIKE";

    String TOPIC_FOLLOW = "FOLLOW";

    String TOPIC_PUBLISH = "PUBLISH";

    String TOPIC_DELETE = "DELETE";

    String TOPIC_SHARE = "SHARE";

    /**
     * 权限: 普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 权限: 管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 权限: 版主
     */
    String AUTHORITY_MODERATOR = "moderator";
}
