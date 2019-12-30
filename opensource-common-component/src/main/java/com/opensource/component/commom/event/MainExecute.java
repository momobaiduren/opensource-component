package com.opensource.component.commom.event;

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
        applicationEventContext.addApplicationListener(new BaseEventListener() {
            /**
             * description �����ص����߼�
             */
            @Override
            void onEventListener( EventSource eventSource ) {
                System.out.println("ִ���¼�,����"+eventSource);
            }
        });
        applicationEventContext.publishEvent(new EventSource("zhanglong"));
    }

}
