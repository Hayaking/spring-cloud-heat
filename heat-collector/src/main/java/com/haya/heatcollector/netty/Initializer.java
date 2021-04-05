package com.haya.heatcollector.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author haya
 */
@Component
@Slf4j
public class Initializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private Handler handler;
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //添加编解码
        socketChannel.pipeline()
                .addLast( "decoder", new StringDecoder( CharsetUtil.UTF_8 ) )
                .addLast( "encoder", new StringEncoder( CharsetUtil.UTF_8 ) )
                .addLast( handler );
    }
}
