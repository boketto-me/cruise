package org.boketto.cruise_server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//参考：https://netty.io/wiki/user-guide-for-4.x.html
public class NettyTransport implements NetworkTransport {

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    @Override
    public void startNetwork() {
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 此处绑定服务器，并等待绑定完成。对sync()方法的调用将导致当前Thread阻塞，直到绑定完成
            ChannelFuture channelFuture = serverBootstrap.bind(9527).sync();
            // 由于调用了sync()方法，程序将会阻塞等待，直到服务器的Channel关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void stopNetwork() {
        try {
            if (workerEventLoopGroup != null) {
                workerEventLoopGroup.shutdownGracefully().sync();
            }
            if (bossEventLoopGroup != null) {
                bossEventLoopGroup.shutdownGracefully().sync();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void rebootNetwork() {
        this.stopNetwork();
        this.startNetwork();
    }

}
