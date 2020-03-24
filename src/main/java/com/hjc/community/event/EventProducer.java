package com.hjc.community.event;

import com.alibaba.fastjson.JSONObject;
import com.hjc.community.entity.Event;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Classname EventProducer
 * @Description TODO
 * @Date 2020-03-17 11:55 a.m.
 * @Created by Justin
 */
@Component
public class EventProducer {

    @Autowired
    KafkaTemplate kafkaTemplate;

    //    处理事件
    public void fireEvent(Event event) {
//        将事件发送到指定主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
