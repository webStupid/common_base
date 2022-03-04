package com.wwb.commonbase.mq.impl.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.wwb.commonbase.mq.IMessageQueueProducer;
import com.wwb.commonbase.mq.MessageQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * RocketMq 消息生产者
 *
 * @author xxx
 */
@Slf4j
public class RocketMqProducer<T> implements IMessageQueueProducer<T> {

    private DefaultMQProducer defaultMQProducer;

    private RocketMqConfig rocketMqConfig;

    private RocketMqTemplate rocketMqTemplate;

    public RocketMqProducer(RocketMqConfig rocketMqConfig) {
        this(rocketMqConfig, null);
    }

    public RocketMqProducer(RocketMqConfig rocketMqConfig, RocketMqTemplate<T> rocketMqTemplate) {
        this.rocketMqTemplate = rocketMqTemplate == null ? new RocketMqTemplate<T>(Object.class) : rocketMqTemplate;
        this.rocketMqConfig = rocketMqConfig;
        defaultMQProducer = new DefaultMQProducer(rocketMqConfig.getGroupName());
        defaultMQProducer.setNamesrvAddr(rocketMqConfig.getNamesrvAddr());
        defaultMQProducer.setRetryTimesWhenSendFailed(1);
        if (StringUtils.isNotBlank(rocketMqConfig.getInstanceName())) {
            defaultMQProducer.setInstanceName(rocketMqConfig.getInstanceName());
        }
        try {
            defaultMQProducer.start();
        } catch (Exception e) {
            log.error("启动生产者失败：", e);
        }
    }

    /**
     * 消息队列配置
     *
     * @return
     */
    @Override
    public MessageQueueConfig getMessageQueueConfig() {
        return rocketMqConfig;
    }

    /**
     * 发送消息
     *
     * @param topic 消息主题
     * @param tag   消息标签
     * @param msg   消息内容
     */
    @Override
    public boolean sendMsg(String topic, String tag, T msg) {

        return sendDelayMsg(topic, tag, msg, 0);
    }

    /**
     * 发送延迟消息
     *
     * @param topic       消息主题
     * @param tag         消息标签
     * @param msg         消息内容
     * @param delaySecond 延迟的秒数 [1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h]对应的序号(从1开始)
     */
    @Override
    public boolean sendDelayMsg(String topic, String tag, T msg, int delaySecond) {
        byte[] json = rocketMqTemplate.toJsonBytes(msg);
        Message message = null;
        if (StringUtils.isNotBlank(tag)) {
            message = new Message(topic, tag, json);
        } else {
            message = new Message(topic, json);
        }
        log.info("发送消息:{},{},{}", topic, tag, JSONObject.toJSON(msg));
        Integer delayLevel = null;
        if (delaySecond > 0) {
            delayLevel = getDelayLevel(delaySecond);
            if (delayLevel > 0) {
                message.setDelayTimeLevel(delayLevel);
            }
        }
        try {
            if (rocketMqConfig.getProxy() != null && rocketMqConfig.getProxy()) {
                return MqProducerProxy.sendMq(rocketMqConfig, json, tag, delayLevel);
            } else {
                defaultMQProducer.send(message, 100000);
                return true;
            }
        } catch (Exception e) {
            log.error("发送消息失败:" + json, e);
            return false;
        }
    }

    /**
     * "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
     */
    static final int[] delayLevels = {1, 5, 10, 30, 60, 120, 180, 240, 300, 360, 420, 480, 540, 600, 1200, 1800, 3600, 7200};

    /**
     * 找到接近的延迟等级（算法可以进一步优化）
     */
    private int getDelayLevel(int delaySecond) {
        int level = 0;
        for (int i = 0; i < delayLevels.length; i++) {
            if (delaySecond == delayLevels[i]) {
                level = i + 1;
                break;
            }
            if (delaySecond > delayLevels[i]) {
                continue;
            }
            //向下靠拢
            level = i;
            break;
        }
        return level;
    }
}
