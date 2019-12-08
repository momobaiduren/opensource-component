package com.opensouce.component.netty.client;

import com.opensouce.component.netty.task.WorkTaskQueue;
import io.netty.channel.Channel;
import javafx.util.Pair;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangLong on 2019/12/8  2:58 下午
 * @version V1.0
 */
public final class ChannelClientCache<ReqData> {
    private final Map<String, Pair<Channel, WorkTaskQueue<ReqData>>> channelClientPool = new ConcurrentHashMap<>();


    public void release(InetSocketAddress inetSocketAddress) {
        String key = getKey(inetSocketAddress);
        channelClientPool.remove(key);
    }

    public WorkTaskQueue<ReqData> registerChannel(InetSocketAddress inetSocketAddress, Channel channel) {
        WorkTaskQueue<ReqData> reqDataWorkTaskQueue = new WorkTaskQueue<>();
        channelClientPool.put(getKey(inetSocketAddress), new Pair<>(channel, reqDataWorkTaskQueue));
        return reqDataWorkTaskQueue;
    }


    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        String key = getKey(inetSocketAddress);
        return channelClientPool.get(key).getKey();
    }

    public void consumerTask(InetSocketAddress inetSocketAddress, ReqData reqData) {
        channelClientPool.get(getKey(inetSocketAddress)).getValue().addTask(reqData);
    }

    private static String getKey(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.getHostName() + ":" + inetSocketAddress.getPort();
    }

}
