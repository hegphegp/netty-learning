package com.hegp;

import com.hegp.common.codec.MessageDecoder;
import com.hegp.common.codec.MessageEncoder;
import com.hegp.handler.ClientBusinessHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ClientConfig {
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    private Channel channel;
    private ChannelFuture channelFuture;

    public ClientConfig() { }

    public void connect(String host, int port) {
        try {
            eventLoopGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline()
                       .addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS))
                       .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 10, 1, 4, 0, 0, true))
                       .addLast(new MessageDecoder())
                       .addLast(new ClientBusinessHandler())
                       .addLast(new MessageEncoder());  //给服务端发送数据时编码
                 }
             });

            //异步连接到服务
            channelFuture = bootstrap.connect(host, port).syncUninterruptibly();
            channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public void destroy() {
        if (null != channel) {
            channel.close();
        }
        if (eventLoopGroup!=null) {
            eventLoopGroup.shutdownGracefully();
        }
    }


}
