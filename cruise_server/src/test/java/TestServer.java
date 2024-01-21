import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.boketto.cruise_server.network.OutboundMessage;
import org.boketto.cruise_server.network.OutboundMessageEncoder;

//参考：https://netty.io/wiki/user-guide-for-4.x.htmlgit
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) {
                            //编码器是一种特殊的出站处理器，必须与服务端的解码器类型一致
                            //一般情况下，应该将编码器作为出站处理器链条的最后一个处理器
                            //编码器缺失，或者与服务端解码器类型不一致，会导致服务端读不到消息
                            channel.pipeline().addLast(new OutboundMessageEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9527).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    channelFuture.channel().writeAndFlush(new OutboundMessage());
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception exception) {

        } finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }
}
