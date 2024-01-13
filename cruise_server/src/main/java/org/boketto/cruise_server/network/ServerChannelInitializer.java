package org.boketto.cruise_server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class ServerChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new ServerInboundHandler());
    }

}
