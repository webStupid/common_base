package com.wwb.commonbase.mq.impl.rocketmq;

import com.wwb.commonbase.mq.MessageQueueConfig;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 消息队列配置信息
 * @author xxx
 */
@Data
@Builder
public class RocketMqConfig extends MessageQueueConfig {

    @Tolerate
    public  RocketMqConfig(){}
    /**
     * 组名
     * */
    private String groupName;

    /**
     * 服务器
     * */
    private String namesrvAddr;


    /**
     * 实例名
     * */
    private String instanceName;

    /**
     * 是否使用代理发送
     * */
    private Boolean proxy;

}
