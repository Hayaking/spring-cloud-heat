package com.haya.heatcollector.netty;

import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.handle.MetricHandle;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @author haya
 */
@Component
@Sharable
@Slf4j
public class Handler extends ChannelInboundHandlerAdapter {
    @Autowired
    private MetricHandle metricHandle;

    /**
     * 客户端连接会触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info( "Channel active......" );
    }

    /**
     * 客户端发消息会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println(msg);
        HeatData heatData = JSON.parseObject( String.valueOf( msg ), HeatData.class );
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        int port = address.getPort();
        heatData.setIp( ip );
        heatData.setPort( port );
        metricHandle.handle(heatData );
        ctx.flush();
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
