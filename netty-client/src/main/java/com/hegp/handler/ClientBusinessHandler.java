package com.hegp.handler;

import com.hegp.entity.Message;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageEntity msg) throws Exception {
        System.out.println("客户端收到消息：" + msg.getBody());
    }

    // 当读取不到消息后时触发（注：会受到粘包、断包等影响，所以未必是客户定义的一个数据包读取完成即调用）
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客服端读取服务端下发消息完毕！");
    }

    // 异常捕捉方法
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端通道异常，异常消息：" + cause.getMessage());
        ctx.close();
    }
}
*/

public class ClientBusinessHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message customMsg) throws Exception {
        /** 接收到服务器的信息后，进行处理 */
//        System.out.println(String.format("ip:%s %s", channelHandlerContext.channel().remoteAddress(), customMsg));
        System.out.println("接收到服务器的信息是"+customMsg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                System.out.println("客户端发送心跳包");
                //向服务端发送消息
//                String message = String.format("client %s", System.currentTimeMillis());
//                byte[] body = (message+"abc").getBytes();
                Message msgEntity = new Message((byte) 0xAB, (byte) 0xCD, "".getBytes());
                ctx.channel().writeAndFlush(msgEntity);
                // writeAndFlush
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
