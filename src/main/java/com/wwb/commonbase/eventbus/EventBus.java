package com.wwb.commonbase.eventbus;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.Executor;

/**
 * @author 异步时间总线
 */
public class EventBus {
    /**
     * 事件任务总线
     */
//    private final static EventBus tiemEventBus = new EventBus();

    private static int maxPoolSize = 1500;

    private static AsyncEventBus asyncEventBus;

    private static Executor executor = new Executor() {
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    };

    /**
     * getAsyncEventBus
     */
    private static AsyncEventBus getAsyncEventBus() {
        if (asyncEventBus == null) {
            synchronized (AsyncEventBus.class) {
                if (asyncEventBus == null) {
                    asyncEventBus = new AsyncEventBus(executor);
                }
            }
        }
        return asyncEventBus;
    }

    /**
     * 触发异步事件
     *
     * @param event
     */
    public static void post(Object event) {
        getAsyncEventBus().post(event);
    }

    /**
     * 注册事件处理器
     *
     * @param handler
     */
    public static void register(Object handler) {
        getAsyncEventBus().register(handler);
    }

    /**
     * 注销事件处理器
     *
     * @param handler
     */
    public static void unregister(Object handler) {
        getAsyncEventBus().unregister(handler);
    }
}
