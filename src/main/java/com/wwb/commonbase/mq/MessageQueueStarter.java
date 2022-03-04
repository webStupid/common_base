package com.wwb.commonbase.mq;

import com.wwb.commonbase.mq.annotation.MQConsumerStarter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列启动器
 * @author xxxx
 */
@Slf4j
@Aspect
public class MessageQueueStarter implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        startConsumers();
    }

    private void startConsumers() {
        if (CollectionUtils.isEmpty(consumerStarters)) {
            log.info("consumerStarters is null");
            return;
        }
        for (Map.Entry<Object, MQConsumerStarter> item : consumerStarters.entrySet()) {
            Class<?> clazz = item.getKey().getClass();
            try {
                Method method = clazz.getMethod(item.getValue().methodName());
                if (method == null) {
                    return;
                }
                log.info("执行"+clazz.getName()+"的"+item.getValue().methodName()+"的方法");
                method.invoke(item.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private   final Map<Object,MQConsumerStarter> consumerStarters = new HashMap<>();

    @Pointcut("@annotation(com.wwb.commonbase.mq.annotation.MQConsumerStarter)")  //@annotation声明以注解的方式来定义切点
    public void checkDataPoint() {

    }

    @AfterReturning(value = "checkDataPoint()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MQConsumerStarter consumerStarter = signature.getMethod().getAnnotation(MQConsumerStarter.class);
        if (consumerStarter == null) {
            return;
        }
        if (StringUtils.isBlank(consumerStarter.methodName())) {
            return;
        }
        consumerStarters.put(result,consumerStarter);
    }
}
