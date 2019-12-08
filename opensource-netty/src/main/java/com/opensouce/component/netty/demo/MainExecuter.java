package com.opensouce.component.netty.demo;

import com.opensouce.component.netty.client.ChannelClientCache;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangLong on 2019/12/8  4:09 下午
 * @version V1.0
 */
public class MainExecuter {
    public static void main(String[] args) throws InterruptedException {
        ChannelClientCache<String> channelClientCache = new ChannelClientCache<>();
        InetSocketAddress localhost = new InetSocketAddress("localhost", 8112);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(()-> {
            new DemoDealWithHandler(localhost, channelClientCache);
        });
        TimeUnit.SECONDS.sleep(3L);
        executorService.execute(()->{
            for (int i = 0; i < 10; i++) {
                channelClientCache.consumerTask(localhost, "zhanglong");
            }
        });
    }
}
