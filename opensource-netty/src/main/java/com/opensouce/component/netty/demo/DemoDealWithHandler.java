package com.opensouce.component.netty.demo;

import com.opensouce.component.netty.client.ChannelClientCache;
import com.opensouce.component.netty.client.DealWithHandler;
import com.opensouce.component.netty.message.ServerResponseClient;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/8  4:04 下午
 * @version V1.0
 */
public class DemoDealWithHandler extends DealWithHandler<String, String> {

    public DemoDealWithHandler(InetSocketAddress inetSocketAddress, ChannelClientCache<String> channelClientCache) {
        super(inetSocketAddress, channelClientCache);
    }

    @Override
    protected BiConsumer<ChannelHandlerContext, ServerResponseClient<String>> dealWithRespData() {
        return (channelHandlerContext, serverResponseClient) -> {
            if ("zhanglong".equals(serverResponseClient.getData())){
                System.out.println("server:" + serverResponseClient.getData());
                sendMsgToServer("lisi");
            }
        };
    }
}
