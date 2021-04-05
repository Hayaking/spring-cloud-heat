package com.haya.heatcollector.netty;

import com.alibaba.fastjson.JSON;
import com.haya.heatcollector.bean.HeatData;
import com.haya.heatcollector.handle.MetricHandle;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haya
 */
@Component
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
        List<HeatData> heatData = JSON.parseArray( msg.toString(), HeatData.class );
        metricHandle.handle(heatData);
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
