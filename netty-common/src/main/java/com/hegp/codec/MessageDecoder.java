package com.hegp.codec;

import com.hegp.entity.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("0000000000000000000000000000");
        ByteBuf buf = (ByteBuf) msg;
        byte type = buf.readByte();
        byte flag = buf.readByte();
        int length = buf.readInt();
        int len = buf.readableBytes();
        byte[] req = new byte[len];
        buf.readBytes(req);
        ctx.fireChannelRead(new Message(type, flag, req));
    }
}
