package com.opensouce.component.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @author ZhangLong on 2019/12/7  8:57 下午
 * @version V1.0
 */
public class CustomEncoder extends MessageToByteEncoder {
    private Class<?> genericClass;

    public CustomEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out) throws Exception {
        if (genericClass.isInstance(obj)) {
            byte[] data = SerializationUtil.serialize(obj);
            //byte[] data = JsonUtil.serialize(in); // Not use this, have some bugs
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
