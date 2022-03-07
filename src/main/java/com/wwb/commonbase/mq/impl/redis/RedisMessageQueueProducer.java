package com.wwb.commonbase.mq.impl.redis;

import com.alibaba.fastjson.JSON;
import com.wwb.commonbase.mq.IMessageQueueProducer;
import com.wwb.commonbase.mq.MessageQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * @author xxx
 */
@Slf4j
public class RedisMessageQueueProducer<T> implements IMessageQueueProducer<T> {

    private RedisTemplate<String, T> redisTemplate;

    private MessageQueueConfig messageQueueConfig;

    public RedisMessageQueueProducer(MessageQueueConfig messageQueueConfig, RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.messageQueueConfig = messageQueueConfig;
    }

    private String realTopic(String topic, String tag) {
        if (StringUtils.isBlank(tag)) {
            return topic;
        }
        return topic + "_" + tag;
    }

    /**
     * 消息队列配置
     *
     * @return
     */
    @Override
    public MessageQueueConfig getMessageQueueConfig() {
        return messageQueueConfig;
    }

    @Override
    public boolean sendMsg(String topic, String tag, T msg) {
        try {
            long ret = redisTemplate.opsForList().leftPush(realTopic(topic, tag), msg);
            return ret > 0;
        } catch (Exception e) {
            log.error("消息推送到队列失败：" + JSON.toJSONString(msg), e);
            return false;
        }
    }

    @Override
    public boolean sendDelayMsg(String topic, String tag, T msg, int delaysecond) {
        long time = System.currentTimeMillis() / 1000;
        try {
            return redisTemplate.opsForZSet().add(realTopic(topic, tag), msg, time + delaysecond);
        } catch (Exception e) {
            log.error("消息推送到队列失败：" + JSON.toJSONString(msg), e);
            return false;
        }
    }
}
