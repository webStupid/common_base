package com.wwb.commonbase.mq.impl.redis;

import com.wwb.commonbase.mq.IMessageQueueConsumer;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

/**
 * Redis 普通消息队列消费者
 *
 * @author xxx
 */
public class RedisMessageQueueConsumer<T> extends IMessageQueueConsumer<T> {

    private RedisTemplate<String, T> redisTemplate;

    public RedisMessageQueueConsumer(RedisTemplate<String, T> redisTemplate, String topic, String tag, Function<T, Void> callback) {
        super(topic, tag, callback);
        this.redisTemplate = redisTemplate;
    }

//    public RedisMessageQueueConsumer(RedisTemplate<String, T> redisTemplate, String topic, Function<T, Void> callback) {
//        this(redisTemplate,topic,null, callback);
//    }

    @Override
    protected Collection<T> readMessage() {
        T msg = redisTemplate.opsForList().rightPop(getRealTopic());
        if (msg == null) {
            return null;
        } else {
            return new ArrayList<T>() {{
                add(msg);
            }};
        }
    }

    @Override
    protected boolean ackMessage(T message) {
        return true;
    }
}
