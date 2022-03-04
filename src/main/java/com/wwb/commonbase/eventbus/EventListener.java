package com.wwb.commonbase.eventbus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author weibo
 */
@Slf4j
public abstract class EventListener<T extends EventArgs> implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        ConfigurableApplicationContext applicationContext = applicationPreparedEvent.getApplicationContext();
        EventListener bean = applicationContext.getBean(this.getClass());
        log.info("regist listener to eventBus...." + bean);
        EventBus.register(bean);
    }



    @Subscribe
    public void on(EventArgs eventArgs) {
        if (eventArgs == null) {
            return;
        }
        if (eventArgs.getClass() != eventArgsClass()) {
            return;
        }
        onReceived((T) eventArgs);
    }

    /**
     * 事件参数类型
     */
    public abstract Class<?> eventArgsClass();

    /**
     * 收到消息，执行相关事件
     *
     * @param eventArgs
     */
    public abstract void onReceived(T eventArgs);
}
