package com.xsl.myeventbus.core;

import java.lang.reflect.Method;

// 注册类中的注册方法信息
public class SubscribleMethod {

    // 注册方法
    private Method method;

    // 线程类型
    private MyThreadMode threadMode;

    // 事件类型
    private Class<?> eventType;

    public SubscribleMethod(Method method, MyThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public MyThreadMode getThreadMode() {
        return threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
