package com.hegp;

import com.hegp.codec.MessageDecoder;
import com.hegp.codec.MessageEncoder;
import com.hegp.entity.Message;
import com.hegp.handler.ClientBusinessHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientApp {
    private EventLoopGroup eventLoopGroup;
    private Channel clientChannel;
    public ClientApp() { }
    public void start(String host, int port) {
        boolean occurredException = false;
        try {
            eventLoopGroup= new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline()
                       .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 10, 2, 4, 0, 0, true))
                       .addLast(new MessageDecoder())
                       .addLast(new ClientBusinessHandler())
                       .addLast(new MessageEncoder());  //给服务端发送数据时编码
                 }
             });

            //异步连接到服务
            ChannelFuture future = bootstrap.connect(host, port).sync();

            clientChannel = future.channel();

            clientChannel.closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            occurredException = true;
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            if (occurredException==true) {
                if (eventLoopGroup!=null) {
                    eventLoopGroup.shutdownGracefully();
                    System.out.println("Client Exit");
                }
            }
        }
    }

    public void sendMsg() {
        int currentCount = 10;
        try {
            while (currentCount > 0) {
                Thread.sleep(10);
                String message = String.format("client %s", System.currentTimeMillis());
                byte[] body = message.getBytes();
                Message msgEntity = new Message((byte) 0xAB, (byte) 0xCD, body);
                //发送数据
                clientChannel.writeAndFlush(msgEntity);
                //System.out.println(msgBody);
                currentCount -= 1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        if (eventLoopGroup!=null) {
            eventLoopGroup.shutdownGracefully();
            System.out.println("Client Exit");
        }
    }

    public static void main(String[] args) throws Exception {
        ClientApp clientApp = new ClientApp();
        clientApp.start("127.0.0.1", 9123);
        clientApp.sendMsg();
//        clientApp.exit();
    }
}