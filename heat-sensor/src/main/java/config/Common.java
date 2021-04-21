package config;


import bean.HeatData;
import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public class Common {
    private static final BlockingQueue<HeatData> queue = new LinkedBlockingDeque<>();

    public static BlockingQueue<HeatData> getQueue() {
        return queue;
    }
}
