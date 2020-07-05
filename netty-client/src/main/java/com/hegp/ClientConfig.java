package com.hegp;

import com.hegp.codec.MessageDecoder;
import com.hegp.codec.MessageEncoder;
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
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();

    private Channel clientChannel;
    public ClientConfig() { }
    public void start(String host, int port) {
        try {
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
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            clientChannel = channelFuture.channel();
            ClientMsgThread thread = new ClientMsgThread(clientChannel);
            thread.start();
            clientChannel.closeFuture().sync();
//            channelFuture.channel().closeFuture().sync(); // 没有 .closeFuture().sync() 这句，程序不会阻塞，直接运行结束

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            if (eventLoopGroup!=null) {
                eventLoopGroup.shutdownGracefully();
                System.out.println("ClientConfig Exit");
            }
        }
    }

    public void exit() {
        if (eventLoopGroup!=null) {
            eventLoopGroup.shutdownGracefully();
            System.out.println("ClientConfig Exit");
        }
    }

    public static void main(String[] args) throws Exception {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.start("127.0.0.1", 9123);
    }
}
