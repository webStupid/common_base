package com.wwb.commonbase.mq.annotation;


import java.lang.annotation.*;

/**
 * MQ消费者启动方法
 * @author xxx
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQConsumerStarter {

    String methodName() default "start";
}
