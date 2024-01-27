package org.boketto.cruise_client.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) {
        //解码器是一种特殊的入站处理器，必须与客户端的编码器类型一致
        //一般情况下，应该将解码器作为入站处理器链条里的第一个处理器
        //解码器缺失，或者与客户端编码器类型不一致，不会触发后面的入站处理器
        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder = new LengthFieldBasedFrameDecoder(5 * 1024 * 1024, 0, 4);
        channel.pipeline().addLast(lengthFieldBasedFrameDecoder);
        channel.pipeline().addLast(new InboundMessageDecoder());
        channel.pipeline().addLast(new ClientInboundHandler());
        channel.pipeline().addLast(new OutboundMessageEncoder());
    }

}
