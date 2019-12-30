package com.opensource.component.commom.event;

import java.util.HashSet;
import java.util.Set;

/**
 * created by zhanglong and since  2019/12/30  4:41 下午
 *
 * @description: 描述
 */
public class ApplicationEventContext {
    /**
     * description
     */
    Set<BaseEventListener> baseEventListeners = new HashSet<>();

    /**
     * 添加监听器
     * @param listener 监听器
     */
    public void addApplicationListener( BaseEventListener listener) {
        this.baseEventListeners.add(listener);
    }

    /**
     * 发布事件
     * 回调所有监听器的回调方法
     * @param eventSource 事件
     */
    public void publishEvent( EventSource eventSource) {
        for (BaseEventListener baseEventListener : baseEventListeners) {
            baseEventListener.onEventListener(eventSource);
        }
    }
}
