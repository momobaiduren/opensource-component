package com.opensouce.component.netty.client;

import com.opensouce.component.netty.message.ServerResponseClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/7  2:12 下午
 * @version V1.0
 */
public class SimpleClientChannelHandler<RespData> extends SimpleChannelInboundHandler<ServerResponseClient<RespData>> {

    private BiConsumer<ChannelHandlerContext, ServerResponseClient<RespData>> contextRespTBiConsumer;

    public SimpleClientChannelHandler(BiConsumer<ChannelHandlerContext, ServerResponseClient<RespData>> contextRespTBiConsumer) {
        this.contextRespTBiConsumer = contextRespTBiConsumer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerResponseClient<RespData> responseData) throws Exception {
        if (null != contextRespTBiConsumer) {
            contextRespTBiConsumer.accept(ctx, responseData);
        }
    }

}
