package com.opensource.component.commom.event;
import java.util.EventListener;

/**
 * @author zhanglong and since  2019/12/30  4:42 ����
 * @description: �¼�������
 */
public abstract class BaseEventListener implements EventListener {
    /**
     * description �����¼�Դ
     * @param eventSource �¼�Դ
     */
    abstract void onEventListener( EventSource eventSource );
}
