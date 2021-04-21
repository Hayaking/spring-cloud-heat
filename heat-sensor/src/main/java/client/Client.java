package client;

import config.Producer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * @author haya
 */

public class Client {

    public void start(String ip, Integer port, List<Thread> tasksList) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group( group )
                .channel( NioSocketChannel.class )
                .handler( new Initializer() );
        try {
            ChannelFuture future = bootstrap.connect( ip, port ).sync();
            Producer producer = new Producer();
            producer.setChannel(future.channel());
            producer.start();
            tasksList.forEach(Thread::start);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
