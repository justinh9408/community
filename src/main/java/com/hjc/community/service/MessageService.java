package com.hjc.community.service;

import com.hjc.community.dao.MessageMapper;
import com.hjc.community.entity.Message;
import com.hjc.community.util.SensitivityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Classname MessageService
 * @Description TODO
 * @Date 2020-02-27 7:24 p.m.
 * @Created by Justin
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitivityFilter sensitiveFilter;

    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    public int insertMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int updateMsgStatus(List<Integer> ids, int status) {
        return messageMapper.updateStatus(ids, status);
    }

    //  查询某个主题最新的message
    public Message findLatestMsg(int userId, String topic){
        return messageMapper.selectLatestMsg(userId, topic);
    }

    //  查询某个主题总通知数量
    public int findNoticeCount(int userId, String  topic){
        return messageMapper.selectNoticeCount(userId, topic);
    }

    //    查询未读的通知数量
    public int findNoticeUnreadCount(int userId, String  topic){
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }
}
