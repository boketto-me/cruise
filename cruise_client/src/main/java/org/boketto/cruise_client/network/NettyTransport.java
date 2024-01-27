package org.boketto.cruise_client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

//参考：https://netty.io/wiki/user-guide-for-4.x.html
public class NettyTransport implements NetworkTransport {

    private EventLoopGroup eventLoopGroup;

    @Override
    public void startNetwork() {
        eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9527).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    channelFuture.channel().writeAndFlush(new OutboundMessage());
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exception) {

        }
    }

    @Override
    public void stopNetwork() {
        try {
            if (eventLoopGroup != null) {
                eventLoopGroup.shutdownGracefully().sync();
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
