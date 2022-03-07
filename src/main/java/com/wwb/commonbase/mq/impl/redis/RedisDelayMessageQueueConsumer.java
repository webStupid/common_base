package com.wwb.commonbase.mq.impl.redis;

import com.wwb.commonbase.mq.IMessageQueueConsumer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * Redis延迟消息队列消费者
 *
 * @author xxx
 */
@SuppressWarnings("unchecked")
public class RedisDelayMessageQueueConsumer<T> extends IMessageQueueConsumer<T> {

    private RedisTemplate<String, T> redisTemplate;

    public RedisDelayMessageQueueConsumer(RedisTemplate<String, T> redisTemplate, String topic, String tag, Function<T, Void> callback) {
        super(topic, tag, callback);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Collection<T> readMessage() {
        long score = System.currentTimeMillis() / 1000;
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        Set<T> messageEntities = zSetOperations.rangeByScore(getRealTopic(), 0, score);
        return messageEntities;
    }

    @Override
    protected boolean ackMessage(T message) {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.remove(getRealTopic(), message) > 0;
    }
}
