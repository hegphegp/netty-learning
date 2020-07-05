package com.hegp;

import com.hegp.common.codec.MessageDecoder;
import com.hegp.common.codec.MessageEncoder;
import com.hegp.handler.ServerBusinessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * ChannelInboundHandler   接收消息的handler， 自己被动读取别人发送过来的消息的处理handler
 * ChannelOutboundHandler  发送消息的handler， 自己给别人发送消息
 */

/**
 * http://blog.csdn.net/linuu/article/details/51371595
 * http://blog.163.com/linfenliang@126/blog/static/127857195201210821145721/
 *
 * 重连
 * http://blog.csdn.net/z69183787/article/details/52625095
 * http://blog.csdn.net/chdhust/article/details/51649184
 */
@Component
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(ServerBusinessHandler.class);
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;

    public NettyServer() { }

    public void bind(int port) {
        try {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    /**
                                     * new IdleStateHandler(50,70,100, TimeUnit.SECONDS)
                                     * netty官方提供的心跳检测类 IdleStateHandler 的构造函数 public IdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit)
                                     * readerIdleTime: 定义的服务器端读事件的时间，当客户端50s时间没有往服务器写数据（服务器端就是读操作）则触发IdleStateEvent事件
                                     * writerIdleTime: 服务器端写事件的时间，当服务器端70s的时间没有向客户端写数据，则触发IdleStateEvent事件
                                     * allIdleTime   : 当客户端没有往服务器端写数据和服务器端没有往客户端写数据100s的时间，则触发IdleStateEvent事件
                                     */
                                    // 心跳检测，处理空闲状态事件的处理器，在new ServerBusinessHandler()处理心跳逻辑
                                    .addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS))
                                    .addLast(new LengthFieldBasedFrameDecoder(1024 * 1024 * 10, 1, 4, 0, 0, true))
                                    .addLast(new MessageDecoder())
                                    .addLast(new ServerBusinessHandler())
                                    .addLast(new MessageEncoder());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            logger.info("绑定端口,开始接收TCP连接");
            channelFuture = sbs.bind(port).syncUninterruptibly();
            channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (null != channel) {
            channel.close();
        }
        if (bossGroup!=null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup!=null) {
            workerGroup.shutdownGracefully();
        }
    }
}
