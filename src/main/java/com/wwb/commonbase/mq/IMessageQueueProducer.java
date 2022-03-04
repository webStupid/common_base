package com.wwb.commonbase.mq;

/**
 * 消息生产者
 * */
public  interface IMessageQueueProducer<T> {

    /**
     * 消息队列配置
     * @return
     * */
    MessageQueueConfig getMessageQueueConfig();

    /**
     * 发送消息
     *
     * @param topic 消息主题
     * @param tag   消息标签
     * @param msg   消息内容
     * @return
     */
    boolean sendMsg(String topic,String tag, T msg);

    /**
     * 发送消息
     *
     * @param topic       消息主题
     * @param tag         消息标签
     * @param msg         消息内容
     * @param delaySecond 延迟的秒数
     * @return
     */
    boolean sendDelayMsg(String topic,String tag,T msg, int delaySecond);
}
