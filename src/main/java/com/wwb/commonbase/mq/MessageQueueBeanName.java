package com.wwb.commonbase.mq;

import java.lang.annotation.*;

/**
 * 生产者BeanName注解
 *
 * @author xxx
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageQueueBeanName {

    String value();
}
