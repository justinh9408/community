package com.hjc.community.util;

/**
 * @Classname RedisUtil
 * @Description TODO
 * @Date 2020-03-04 6:25 p.m.
 * @Created by Justin
 */
public class RedisUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";
    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";
    private static final String PREFIX_POST = "post";

    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    public static String getUVKey(String date) {
        return PREFIX_UV + SPLIT + date;
    }

    public static String getUVKey(String start, String end) {
        return PREFIX_UV + SPLIT + start + SPLIT + end;
    }

    public static String getDAUKey(String date) {
        return PREFIX_DAU + SPLIT + date;
    }

    public static String getDAUKey(String start, String end) {
        return PREFIX_DAU + SPLIT + start + SPLIT + end;
    }

    public static String getPostScoreKey() {
        return PREFIX_POST + SPLIT + "score";
    }
}
