package com.opensource.component.commom;

import com.opensource.component.commom.event.ApplicationEventContext;
import com.opensource.component.commom.event.BaseEventListener;
import com.opensource.component.commom.event.EventSource;

/**
 * @author zhanglong and since  2019/12/30  4:04 ����
 * @description: �����¼�����
 */
public class MainExecute {
    /**
     * description
     * ��һ��ע�������
     * �ڶ��������¼���ִ�лص�
     */
    public static void main( String[] args ) {
        final ApplicationEventContext applicationEventContext = new ApplicationEventContext();
        applicationEventContext.registerEventListener(new BaseEventListener() {
            /**
             * description �����ص����߼�
             */
            @Override
            public void onEventListener( EventSource eventSource ) {
                System.out.println("ִ���¼�,����"+eventSource);
            }
        });
        applicationEventContext.publishEvent(new EventSource("zhanglong"));
    }

}
