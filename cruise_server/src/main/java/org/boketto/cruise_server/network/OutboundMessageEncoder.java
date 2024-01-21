package org.boketto.cruise_server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutboundMessageEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] bytes = ((OutboundMessage)msg).encodeMessage();
        ctx.writeAndFlush(bytes);
        super.write(ctx, msg, promise);
    }

}
