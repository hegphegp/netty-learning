package com.hegp;

import com.hegp.coder.MessageDecoder;
import com.hegp.coder.MessageEncoder;
import com.hegp.entity.Message;
import com.hegp.handler.ClientBusinessHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientApp {
    private String host;
    private int port;
    public ClientApp() { }

    public ClientApp(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
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
            ChannelFuture future = b.connect(host, port).sync();

            Channel clientChannel = future.channel();

            int currentCount = 10;

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

            System.out.println("Close Channel");
            clientChannel.close();
            clientChannel.closeFuture().sync();

            System.out.println("Client Exit");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ClientApp("127.0.0.1", 9123).start();
    }
}