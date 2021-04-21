package task;

import bean.Component;
import bean.HeatData;
import bean.Metric;
import config.Common;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static bean.MetricType.CIRCLE_PUMP;
import static bean.MetricType.MAKE_PUMP;
import static java.util.Arrays.asList;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Pump extends Thread {
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Metric> metricList = asList(
            new Metric("pump_watt", 50d, 100d),
            new Metric("pump_voltage", 0d, 5d),
            new Metric("pump_state", 0d, 3d, Integer.class),
            new Metric("component_up", 0D, 0d, Integer.class)
    );
    private final List<Component> componentList = asList(
            new Component(113.63477, 34.75035, CIRCLE_PUMP.getType()),
            new Component(113.617440, 34.75063, MAKE_PUMP.getType())
    );

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            for (Component component : componentList) {
                for (Metric metric : metricList) {
                    queue.add(HeatData.builder()
                            .time(new Date())
                            .lon(component.getLon())
                            .lat(component.getLat())
                            .type(component.getType())
                            .metricName(metric.getName())
                            .metricValue(metric.getRandomNumber())
                            .build());
                }
            }
            System.out.println("泵发送完");
            TimeUnit.MINUTES.sleep(1);

        }
    }

}