package org.boketto.cruise_server.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

//参考：https://netty.io/wiki/user-guide-for-4.x.html
public class ServerInboundHandler extends SimpleChannelInboundHandler<InboundMessage> {

    private ConcurrentHashMap<String, Channel> activeChannelKV = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String channelId = ctx.channel().id().asLongText();
        activeChannelKV.put(channelId, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        activeChannelKV.remove(ctx.channel().id().asLongText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundMessage inboundMessage) throws Exception {
        Short messageType = inboundMessage.getMessageType();
        if (messageType.intValue() == 0) {
            ctx.channel().writeAndFlush(new OutboundMessage((short) 0, "PONG".getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
