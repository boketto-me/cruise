package org.boketto.cruise_server;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public void start() throws Exception {
        NettyHandler nettyHandler = new NettyHandler();
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(9527))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(nettyHandler);
                        }
                    });
            // 此处绑定服务器，并等待绑定完成。对sync()方法的调用将导致当前Thread阻塞，直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            // 由于调用了sync()方法，程序将会阻塞等待，直到服务器的Channel关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerEventLoopGroup.shutdownGracefully().sync();
            bossEventLoopGroup.shutdownGracefully().sync();
        }
    }

}
