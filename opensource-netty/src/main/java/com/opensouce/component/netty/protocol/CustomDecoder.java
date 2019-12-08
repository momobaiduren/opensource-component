package com.opensouce.component.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author ZhangLong on 2019/12/7  8:57 下午
 * @version V1.0
 */
public class CustomDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public CustomDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4){
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);
    }
}
