package org.boketto.cruise_server.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InboundMessageDecoder extends SimpleChannelInboundHandler<byte[]> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        InboundMessage inboundMessage = new InboundMessage().decodeMessage(bytes);
        System.out.println(inboundMessage.getMagicNumber());
        System.out.println(inboundMessage.getMessageId());
    }
}
