package org.boketto.cruise_server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutboundMessageEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //Server端处理网络报文的第一个解码器便是LengthFieldBasedFrameDecoder，
        //其入参只支持ByteBuf类型，所以客户端的出站编码器需要将数据处理为ByteBuf，
        //具体可以看LengthFieldBasedFrameDecoder解码器类的decode方法实现
        byte[] bytes = ((OutboundMessage)msg).encodeMessage();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        ctx.writeAndFlush(byteBuf);
    }

}
