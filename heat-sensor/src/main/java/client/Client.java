package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import task.MetricTask;

/**
 * @author haya
 */

public class Client {

    public void start(String ip, Integer port, MetricTask task) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group( group )
                //该参数的作用就是禁止使用Nagle算法，使用于小数据即时传输
                .option( ChannelOption.TCP_NODELAY, true )
                .channel( NioSocketChannel.class )
                .handler( new Initializer() );
        try {
            ChannelFuture future = bootstrap.connect( ip, port ).sync();
            task.setChannel( future.channel() );
            task.start();
            task.join();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}
