package com.opensouce.component.netty.message;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author ZhangLong on 2019/12/8  11:52 上午
 * @version V1.0
 */
public abstract class InOutResultData {

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";

    public void sendMsg(ChannelHandlerContext ctx){
        ctx.writeAndFlush(this);
    }

}
