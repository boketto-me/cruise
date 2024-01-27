package org.boketto.cruise_client.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InboundMessageDecoder extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        //LengthFieldBasedFrameDecoder解码之后的返回值是固定的ByteBuf类型
        //具体可以看LengthFieldBasedFrameDecoder解码器类的decode方法实现
        if (byteBuf.isReadable()) {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            InboundMessage inboundMessage = new InboundMessage().decodeMessage(bytes);
            System.out.println(inboundMessage.getMagicNumber());
            System.out.println(inboundMessage.getMessageId());
        }
    }
}
