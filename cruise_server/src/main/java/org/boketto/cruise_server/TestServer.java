package org.boketto.cruise_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

//参考：https://netty.io/wiki/user-guide-for-4.x.htmlgit
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new NettyHandler());
                        }

                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9527).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exception) {

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
