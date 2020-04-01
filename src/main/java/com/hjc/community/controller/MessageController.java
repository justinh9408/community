package com.hjc.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.hjc.community.entity.Message;
import com.hjc.community.entity.Page;
import com.hjc.community.entity.User;
import com.hjc.community.service.MessageService;
import com.hjc.community.service.UserService;
import com.hjc.community.util.CommunityConstant;
import com.hjc.community.util.CommunityUtil;
import com.hjc.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @Classname MessageController
 * @Description TODO
 * @Date 2020-02-27 7:25 p.m.
 * @Created by Justin
 */
@Controller()
@RequestMapping("/letter")
public class MessageController implements CommunityConstant {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String toLetterListPage(Model model, Page page) {
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        // 查询总共未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnread = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnread);

        return "/site/letter";
    }

    @GetMapping("/detail/{conversationId}")
    public String toDetailPage(@PathVariable("conversationId") String conversationId, Model model, Page page) {
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        page.setPath("/letter/detail");
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        // 私信目标
        model.addAttribute("target", getTargetUer(conversationId));
        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }

    private User getTargetUer(String conversationId) {
        String[] ids = conversationId.split("_");
        if (hostHolder.getUser().getId() == Integer.parseInt(ids[0])) {
            return userService.findUserById(Integer.parseInt(ids[1]));
        } else {
            return userService.findUserById(Integer.parseInt(ids[0]));
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    @PostMapping("/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJsonObject(1, "目标用户不存在!");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setContent(content);
        message.setToId(target.getId());
        message.setCreateTime(new Date());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        messageService.addMessage(message);
        return CommunityUtil.getJsonObject(0);
    }

    @GetMapping("/notice/list")
    public String noticeList(Model model) {
        User user = hostHolder.getUser();

//        查询评论类通知
        Message msg = messageService.findLatestMsg(user.getId(), TOPIC_COMMENT);
        Map<String, Object> msgVO = new HashMap<>();
        msgVO.put("message", msg);
        if (msg != null) {

            String content = HtmlUtils.htmlUnescape(msg.getContent());
            HashMap<String, Object> data = JSONObject.parseObject(content, HashMap.class);
            User u = userService.findUserById((Integer) data.get("userId"));
            msgVO.put("user", u);
            msgVO.put("entityType", data.get("entityType"));
            msgVO.put("entityId", data.get("entityId"));
            msgVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
            msgVO.put("count", count);

            int unreadCount = messageService.findNoticeUnreadCount(user.getId(), TOPIC_COMMENT);
            msgVO.put("unread", unreadCount);
        }
        model.addAttribute("commentNotice", msgVO);

//        查询点赞类通知
        msg = messageService.findLatestMsg(user.getId(), TOPIC_LIKE);
        msgVO = new HashMap<>();
        msgVO.put("message", msg);
        if (msg != null) {

            String content = HtmlUtils.htmlUnescape(msg.getContent());
            HashMap<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            msgVO.put("user", userService.findUserById((Integer) data.get("userId")));
            msgVO.put("entityType", data.get("entityType"));
            msgVO.put("entityId", data.get("entityId"));
            msgVO.put("postId", data.get("postId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            msgVO.put("count", count);

            int unreadCount = messageService.findNoticeUnreadCount(user.getId(), TOPIC_LIKE);
            msgVO.put("unread", unreadCount);
        }
        model.addAttribute("likeNotice", msgVO);

//        查询关注类通知
        msg = messageService.findLatestMsg(user.getId(), TOPIC_FOLLOW);
        msgVO = new HashMap<>();
        msgVO.put("message", msg);
        if (msg != null) {

            String content = HtmlUtils.htmlUnescape(msg.getContent());
            HashMap<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            msgVO.put("user", userService.findUserById((Integer) data.get("userId")));
            msgVO.put("entityType", data.get("entityType"));
            msgVO.put("entityId", data.get("entityId"));

            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            msgVO.put("count", count);

            int unreadCount = messageService.findNoticeUnreadCount(user.getId(), TOPIC_FOLLOW);
            msgVO.put("unread", unreadCount);
        }
        model.addAttribute("followNotice", msgVO);

//        查询未读消息数量
        int letterUnread = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnread);
        int noticeUnread = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnread);

        return "/site/notice";
    }

    @GetMapping("/notice/detail/{topic}")
    public String noticeDetail(@PathVariable("topic") String topic, Page page, Model model) {
        User user = hostHolder.getUser();
        page.setPath("/letter/notice/detail/"+topic);
        page.setLimit(5);
        page.setRows(messageService.findNoticeCount(user.getId(), topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if (noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                // 通知
                map.put("notice", notice);
                // 内容
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 通知作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getLetterIds(noticeList);
        if (!ids.isEmpty() ) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }
}
