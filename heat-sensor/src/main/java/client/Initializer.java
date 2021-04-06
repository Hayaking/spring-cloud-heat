package client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Initializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        DelimiterBasedFrameDecoder delimit = new DelimiterBasedFrameDecoder( 1024, Unpooled.copiedBuffer( "$".getBytes() ) );

        socketChannel.pipeline()
                .addLast( delimit )
                .addLast( "decoder", new StringDecoder() )
                .addLast( "encoder", new StringEncoder() )
                .addLast( new Handle() );

    }
}

