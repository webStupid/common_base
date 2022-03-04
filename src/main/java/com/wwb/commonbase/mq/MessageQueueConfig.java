package com.wwb.commonbase.mq;

import lombok.Data;

/**
 * 消息队列配置
 * @author xxx
 */
@Data
public class MessageQueueConfig {

    /**
     * 消息主题
     * */
    private String topic;

    /**
     * Tag
     * */
    private String tag;
}
