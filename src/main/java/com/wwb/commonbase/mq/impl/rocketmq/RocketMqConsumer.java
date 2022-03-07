package com.wwb.commonbase.mq.impl.rocketmq;

import com.wwb.commonbase.mq.IMessageQueueConsumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * RocketMq消费者
 *
 * @author xxx
 */
@Slf4j
public class RocketMqConsumer<T> extends IMessageQueueConsumer<T> {

    private RocketMqConfig rocketMqConfig;

    private RocketMqTemplate rocketMqTemplate;

    DefaultMQPushConsumer defaultMQPushConsumer;

    private Function<Void, Void> onShutdown;

    private Class<T> entityClass;

    public RocketMqConsumer(RocketMqConfig rocketMqConfig, RocketMqTemplate rocketMqTemplate, Function<T, Void> callback) {
        this(rocketMqConfig, rocketMqTemplate, callback, null);
    }

    public RocketMqConsumer(RocketMqConfig rocketMqConfig, Function<T, Void> callback, Function<Void, Void> onShutdown) {
        this(rocketMqConfig, null, callback, onShutdown);
    }

    public RocketMqConsumer(RocketMqConfig rocketMqConfig, Function<T, Void> callback) {
        this(rocketMqConfig, null, callback, null);
    }

    public RocketMqConsumer(RocketMqConfig rocketMqConfig, RocketMqTemplate<T> rocketMqTemplate, Function<T, Void> callback, Function<Void, Void> onShutdown) {
        super(rocketMqConfig.getTopic(), rocketMqConfig.getTag(), callback);

        this.rocketMqConfig = rocketMqConfig;
        if (rocketMqTemplate != null) {
            this.rocketMqTemplate = rocketMqTemplate;
        } else {
            this.rocketMqTemplate = new RocketMqTemplate<T>(Object.class);
        }
        this.onShutdown = onShutdown;
        this.entityClass = rocketMqTemplate.getClazz();
        defaultMQPushConsumer = new DefaultMQPushConsumer(rocketMqConfig.getGroupName());
        defaultMQPushConsumer.setNamesrvAddr(rocketMqConfig.getNamesrvAddr());
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        defaultMQPushConsumer.setInstanceName(rocketMqConfig.getInstanceName());
    }

    boolean registed = false;

    @SneakyThrows
    @Override
    public void run() {
        while (!registed) {
            log.info("启动消费者:" + Thread.currentThread().getName());
            try {
                defaultMQPushConsumer.subscribe(rocketMqConfig.getTopic(), StringUtils.isBlank(rocketMqConfig.getTag()) ? "*" : rocketMqConfig.getTag());
                defaultMQPushConsumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> consumeConcurrentlyStatus(list));
                defaultMQPushConsumer.start();
                registed = true;
                log.info("启动消费者成功:" + Thread.currentThread().getName());
            } catch (Exception e) {
                registed = true;
                log.error("订阅消息失败", e);
                Thread.sleep(1000);
            }
            if (registed) {
                break;
            }
        }
    }

    @Override
    public void shutdown() {
        log.info("消费者被关闭");
        if (defaultMQPushConsumer != null) {
            defaultMQPushConsumer.shutdown();
            if (onShutdown != null) {
                onShutdown.apply(null);
            }
        }
        super.shutdown();
    }

    private ConsumeConcurrentlyStatus consumeConcurrentlyStatus(List<MessageExt> list) {
        try {
            if (!CollectionUtils.isEmpty(list)) {
                for (MessageExt msg : list) {
                    log.info("收到消息:{},{},{}", msg.getTopic(), msg.getTags(), msg.getMsgId());
                    T messageEntity = (T) rocketMqTemplate.toObject(msg.getBody());
                    if (callback != null) {
                        callback.apply(messageEntity);
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理消息异常:类型为：" + entityClass.getName(), e);
            if (e instanceof SerializationException) {
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    protected Collection<T> readMessage() {
        return null;
    }

    @Override
    protected boolean ackMessage(T message) {
        return false;
    }
}
