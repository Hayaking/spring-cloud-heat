package config;


import bean.HeatData;
import lombok.Data;

import java.util.concurrent.*;

@Data
public class Common {
    private static final BlockingQueue<HeatData> QUEUE = new LinkedBlockingDeque<>();
    private static final ExecutorService POOL = Executors.newFixedThreadPool( 10 );
    private static final ScheduledExecutorService SCHEDULED_POOL = Executors.newSingleThreadScheduledExecutor();

    public static ExecutorService getPOOL() {
        return POOL;
    }

    public static ScheduledExecutorService getScheduledPool() {
        return SCHEDULED_POOL;
    }

    public static BlockingQueue<HeatData> getQueue() {
        return QUEUE;
    }
}
