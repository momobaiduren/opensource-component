package com.opensource.component.commom.event;

import java.util.HashSet;
import java.util.Set;

/**
 * created by zhanglong and since  2019/12/30  4:41 ����
 *
 * @description: ����
 */
public class ApplicationEventContext {
    /**
     * description
     */
    Set<BaseEventListener> baseEventListeners = new HashSet<>();

    /**
     * ��Ӽ�����
     * @param listener ������
     */
    public void addApplicationListener( BaseEventListener listener) {
        this.baseEventListeners.add(listener);
    }

    /**
     * �����¼�
     * �ص����м������Ļص�����
     * @param eventSource �¼�
     */
    public void publishEvent( EventSource eventSource) {
        for (BaseEventListener baseEventListener : baseEventListeners) {
            baseEventListener.onEventListener(eventSource);
        }
    }
}
