package client;

import bean.ServerSocket;
import config.Common;
import config.Producer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */

public class Client {

    public void start(Integer bindPort, ServerSocket serverSocket, TimerTask task) {
        Common.getPOOL().execute( ()->{
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap()
                    .group( group )
                    .channel( NioSocketChannel.class )
                    .handler( new Initializer() );
            bootstrap.bind( bindPort );
            try {
                ChannelFuture future = bootstrap.connect( serverSocket.getIp(), serverSocket.getPort() ).sync();
                Producer producer = new Producer();
                producer.setChannel( future.channel() );
                producer.start();
                Common.getScheduledPool().scheduleAtFixedRate( task, 0, 1, TimeUnit.MINUTES );
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        } );

    }

}
