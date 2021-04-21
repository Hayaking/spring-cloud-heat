package config;

import bean.HeatData;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.BlockingQueue;

@EqualsAndHashCode(callSuper = true)
@Data
public class Producer extends Thread{
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    protected Channel channel;

    @Override
    public void run() {
        while (true) {
            HeatData data = queue.poll();
            if (data == null) {
                continue;
            }
            channel.write( JSON.toJSONString( data ) );
            channel.writeAndFlush( "$" );
        }
    }
}
