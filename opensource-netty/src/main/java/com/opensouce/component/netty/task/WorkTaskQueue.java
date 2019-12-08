package com.opensouce.component.netty.task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ZhangLong on 2019/12/8  3:28 下午
 * @version V1.0
 */
public class WorkTaskQueue<ReqData> {
    private final Queue<ReqData> workTaskPool = new ConcurrentLinkedQueue<>();

    public void addTask(ReqData reqData) {
        workTaskPool.offer(reqData);
    }

    public ReqData poll() {
        return workTaskPool.poll();
    }

}
