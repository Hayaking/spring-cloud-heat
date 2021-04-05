package task;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MetricTask extends Thread{
    protected ChannelFuture future;
    protected Channel channel;
}
