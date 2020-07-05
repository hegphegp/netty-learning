package com.hegp.common.codec;

import com.hegp.common.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte type = buf.readByte();
        int len = buf.readInt();
//        int len = buf.readableBytes();
        byte[] req = new byte[len];
        buf.readBytes(req);
        ctx.fireChannelRead(new Message(type, req));
    }
}
