package com.wwb.commonbase.mq;

import com.wwb.commonbase.utils.SpringContextUtil;
import org.apache.commons.lang.StringUtils;

/**
 * 消息
 *
 * @author xxx
 */
public abstract class MessageQueueEntity {

    /**
     * 发送实时消息
     *
     * @param <T>
     */
    public <T extends MessageQueueEntity> void send() throws Exception {
        send(null);
    }

    /**
     * 发送实时消息
     *
     * @param <T>
     * @param customTag 自定义tag
     */
    public <T extends MessageQueueEntity> void send(String customTag) throws Exception {
        IMessageQueueProducer<T> producer = messageQueueProducer();
        MessageQueueConfig messageQueueConfig = producer.getMessageQueueConfig();

        producer.sendMsg(messageQueueConfig.getTopic(), StringUtils.isNotBlank(customTag) ? customTag : messageQueueConfig.getTag(), (T) this);
    }

    /**
     * 发送延迟消息
     *
     * @param <T>
     * @param delaySeconds 延迟秒数,等于0时，及时发送
     */
    public <T extends MessageQueueEntity> void sendDelay(int delaySeconds) throws Exception {
        if (delaySeconds <= 0) {
            send();
            return;
        }
        IMessageQueueProducer<T> producer = messageQueueProducer();
        MessageQueueConfig messageQueueConfig = producer.getMessageQueueConfig();
        producer.sendDelayMsg(messageQueueConfig.getTopic(), messageQueueConfig.getTag(), (T) this, delaySeconds);
    }

    private <T extends MessageQueueEntity> IMessageQueueProducer<T> messageQueueProducer() throws Exception {
        MessageQueueBeanName mqBeanName = getClass().getAnnotation(MessageQueueBeanName.class);
        if (mqBeanName == null) {
            throw new Exception("消息队列对象[" + getClass().getName() + "]未设置MessageQueueBeanName注解");
        }
        IMessageQueueProducer<T> producer = (IMessageQueueProducer<T>) SpringContextUtil.getBean(mqBeanName.value());

        if (producer == null) {
            throw new Exception("未对[" + getClass().getName() + "]创建生产者对象");
        }
        return producer;
    }
}
