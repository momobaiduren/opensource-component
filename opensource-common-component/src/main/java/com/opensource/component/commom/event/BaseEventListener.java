package com.opensource.component.commom.event;
import java.util.EventListener;

/**
 * @author zhanglong and since  2019/12/30  4:42 下午
 * @description: 事件监听器
 */
public abstract class BaseEventListener implements EventListener {
    /**
     * description 监听事件源
     * @param eventSource 事件源
     */
    abstract void onEventListener( EventSource eventSource );
}
