package com.opensouce.component.netty.client;

import com.opensouce.component.netty.message.ClientRequestServer;
import com.opensouce.component.netty.message.ServerResponseClient;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/8  3:14 下午
 * @version V1.0
 */
public abstract class DealWithHandler<ReqData, RespData> {

    private InetSocketAddress inetSocketAddress;
    private ChannelClientCache<ReqData> channelClientCache;

    public DealWithHandler(InetSocketAddress inetSocketAddress, ChannelClientCache<ReqData> channelClientCache) {
        this.inetSocketAddress = inetSocketAddress;
        this.channelClientCache = channelClientCache;
        new NettyClient<>(dealWithRespData(), channelClientCache).connect(inetSocketAddress);
    }

    public void sendMsgToServer(ReqData reqData) {
        ClientRequestServer<ReqData> clientRequestServer = new ClientRequestServer<>();
        clientRequestServer.setData(reqData);
        channelClientCache.getChannel(inetSocketAddress).writeAndFlush(clientRequestServer);
    }

    protected abstract BiConsumer<ChannelHandlerContext, ServerResponseClient<RespData>> dealWithRespData();

    public void sendMsgToServer(ChannelHandlerContext context, ReqData reqData){
        ClientRequestServer<ReqData> clientRequestServer = new ClientRequestServer<>();
        clientRequestServer.setData(reqData);
        context.writeAndFlush(clientRequestServer);
    }
}
