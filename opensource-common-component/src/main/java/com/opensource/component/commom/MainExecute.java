package com.opensource.component.commom;

import com.opensource.component.commom.event.AbstractEventListener;
import com.opensource.component.commom.event.ApplicationEventContext;
import com.opensource.component.commom.event.EventSource;

/**
 * @author zhanglong and since  2019/12/30  4:04 ����
 * @description: �����¼�����
 */
public class MainExecute {

    /**
     * description ��һ��ע������� �ڶ��������¼���ִ�лص�
     */
    public static void main( String[] args ) {
        final ApplicationEventContext<String, EventSource<String>> applicationEventContext = new ApplicationEventContext<>();
        applicationEventContext
            .registerEventListener(new AbstractEventListener<String, EventSource<String>>() {
                /**
                 * description �����ص����߼�
                 */
                @Override
                public void onEventListener( EventSource<String> eventSource ) {
                    System.out.println("ִ���¼�1,����" + eventSource);
                }
            });
        applicationEventContext
            .registerEventListener(new AbstractEventListener<String, EventSource<String>>() {
                /**
                 * description �����ص����߼�
                 */
                @Override
                public void onEventListener( EventSource<String> eventSource ) {
                    System.out.println("ִ���¼�2,����" + eventSource);
                }
            });
        applicationEventContext.publishEvent(new EventSource<>("zhanglong"));
    }

}
