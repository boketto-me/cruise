package org.boketto.cruise_server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;

public class InboundMessageDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        //LengthFieldBasedFrameDecoder解码之后的返回值是固定的ByteBuf类型
        //具体可以看LengthFieldBasedFrameDecoder解码器类的decode方法实现
        try {
            String errorMessage = "";
            do {
                if (!(object instanceof ByteBuf)) {
                    errorMessage = "Message Decode Error";
                    break;
                }
                //解码器（LengthFieldBasedFrameDecoder）已经做了类型转换，这里直接强转就可以了。
                //参考：https://blog.csdn.net/taxi1993/article/details/103140523
                ByteBuf byteBuf = (ByteBuf) object;
                if (!(byteBuf.isReadable())) {
                    errorMessage = "Message Not Readable";
                    break;
                }
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                InboundMessage inboundMessage = InboundMessage.decodeMessage(bytes);
                if (inboundMessage.getMagicNumber().intValue() != 9527) {
                    errorMessage = "Message Not Legal : " + inboundMessage;
                    break;
                }
                if (StringUtils.isNotBlank(errorMessage)) {
                    System.out.println(errorMessage);
                } else {
                    //消息检查通过之后，再手动触发给下一个入站处理器继续处理
                    System.out.println(inboundMessage.toString());
                    channelHandlerContext.fireChannelRead(inboundMessage);
                }
            } while (1 == 0);
        } finally {
            ReferenceCountUtil.release(object);
        }
    }
}
