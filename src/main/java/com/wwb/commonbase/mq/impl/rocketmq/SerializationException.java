package com.wwb.commonbase.mq.impl.rocketmq;

import org.springframework.core.NestedRuntimeException;

/**
 * @author weibo
 */
public class SerializationException extends NestedRuntimeException {
    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}