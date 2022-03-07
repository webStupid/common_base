package com.wwb.commonbase.mq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.function.Function;

/**
 * 消息队列消费者
 *
 * @author weibo
 */
@Slf4j
public abstract class IMessageQueueConsumer<T> extends Thread {

    /**
     * 消息主题
     */
    private String topic;

    /**
     * 消息标签
     */
    private String tag;

    public String getRealTopic() {
        if (StringUtils.isBlank(tag)) {
            return topic;
        }
        return topic + "_" + tag;
    }

    protected Function<T, Void> callback;

    public IMessageQueueConsumer(String topic, String tag, Function<T, Void> callback) {
        this.tag = tag;
        this.topic = topic;
        this.callback = callback;
    }

    private volatile boolean flag = true;

    protected abstract Collection<T> readMessage();

    protected abstract boolean ackMessage(T message);

    private boolean isEmptyMessage(Collection<T> msgs) {
        return msgs == null || msgs.isEmpty();
    }

    @Override
    public void run() {
        try {
            while (flag && !Thread.currentThread().isInterrupted()) {
                try {
                    Collection<T> messageEntities = readMessage();
                    if (isEmptyMessage(messageEntities)) {
                        Thread.sleep(1000);
                        continue;
                    }
                    dealMessage(callback, messageEntities);
                } catch (Exception exception) {
                    // 发生异常，可能是连接redis失败，等待10s重试
                    log.error("消费消息出现异常(topic:" + topic + (StringUtils.isNotBlank(tag) ? (",tag:" + tag) : "") + ")", exception);
                    Thread.sleep(10000);
                }
            }
        } catch (Exception e) {
            log.error("消息队列消费者线程异常退出", e);
        }
    }

    /**
     * 处理消息
     */
    private void dealMessage(Function<T, Void> callback, Collection<T> messageEntities) {

        for (T message : messageEntities) {
            boolean ack = ackMessage(message);
            if (!ack) {
                // 移除队列失败,不执行接下来的逻辑
                log.warn("ACK消息：" + JSON.toJSONString(message) + " 失败");
                continue;
            }
            try {
                if (callback != null) {
                    callback.apply(message);
                }
            } catch (Exception e) {
                log.error("业务处理:" + message + "出错", e);
            }
        }
    }

    /**
     * 退出线程
     */
    public void shutdown() {
        flag = false;
        log.info("正在退出线程(" + this.getName() + ")...");
    }
}
