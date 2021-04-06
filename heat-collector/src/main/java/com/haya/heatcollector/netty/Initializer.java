package com.haya.heatcollector.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.netty.channel.ChannelHandler.Sharable;


/**
 * @author haya
 */
@Component
@Sharable
@Slf4j
public class Initializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private Handler handler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        DelimiterBasedFrameDecoder delimit = new DelimiterBasedFrameDecoder( 1024, Unpooled.copiedBuffer( "$".getBytes() ) );

        socketChannel.pipeline()
                .addLast( delimit )
                .addLast( "decoder", new StringDecoder( CharsetUtil.UTF_8 ) )
                .addLast( "encoder", new StringEncoder( CharsetUtil.UTF_8 ) )
                .addLast( handler );
    }
}
