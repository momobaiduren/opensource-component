package com.opensource.component.commom;

import com.opensource.component.commom.event.AbstractEventListener;
import com.opensource.component.commom.event.ApplicationEventContext;
import com.opensource.component.commom.event.EventSource;

/**
 * @author zhanglong and since  2019/12/30  4:04 下午
 * @description: 基于事件驱动
 */
public class MainExecute {

    /**
     * description 第一步注册监听器 第二部发布事件，执行回调
     */
    public static void main( String[] args ) {
        final ApplicationEventContext<String, EventSource<String>> applicationEventContext = new ApplicationEventContext<>();
        applicationEventContext
            .registerEventListener(new AbstractEventListener<String, EventSource<String>>() {
                /**
                 * description 监听回调的逻辑
                 */
                @Override
                public void onEventListener( EventSource<String> eventSource ) {
                    System.out.println("执行事件1,我是" + eventSource);
                }
            });
        applicationEventContext
            .registerEventListener(new AbstractEventListener<String, EventSource<String>>() {
                /**
                 * description 监听回调的逻辑
                 */
                @Override
                public void onEventListener( EventSource<String> eventSource ) {
                    System.out.println("执行事件2,我是" + eventSource);
                }
            });
        applicationEventContext.publishEvent(new EventSource<>("zhanglong"));
    }

}
