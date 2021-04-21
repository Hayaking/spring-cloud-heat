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

import static bean.MetricType.*;
import static java.util.Arrays.asList;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Pipe extends Thread {
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Component> componentList = asList(
            new Component(113.60017, 34.74740, SUPPLY_PIPE.getType()),
            new Component(113.59426, 34.74733, BACK_PIPE.getType()),
            new Component(113.59131, 34.74740, SUPPLY_SECONDARY_PIPE.getType()),
            new Component(113.58899, 34.74692, BACK_SECONDARY_PIPE.getType()),
            new Component(113.58491, 34.74740, CIRCULATION_PIPE.getType())
    );
    private final List<Metric> metricList = asList(
            new Metric("pipeline_water_temperature", 50d, 100d),
            new Metric("pipeline_water_flow", 50d, 100d),
            new Metric("pipeline_water_pressure", 50d, 100d),
            new Metric("pipeline_water_tassels", 50d, 100d),
            new Metric("pipeline_water_temperature_increase", 50d, 100d),
            new Metric("pipeline_water_pressure_increase", 50d, 100d),
            new Metric("pipeline_water_level", 50d, 100d),
            new Metric("pipeline_out_temperature", 50d, 100d),
            new Metric("pipeline_valve_state", 0d, 3d, Integer.class),
            new Metric("component_up", 0D, 0d, Integer.class)
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
            System.out.println("管道发送完");
            TimeUnit.MINUTES.sleep(1);
        }
    }
}
