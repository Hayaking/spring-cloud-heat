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
public class Sensor extends Thread {
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Component> componentList = asList(
            new Component(113.60017, 34.74740, SENSOR_FLOW.getType(), "flow1"),
            new Component(113.60017, 34.74740, SENSOR_TEMP.getType(), "temperature1"),
            new Component(113.60017, 34.74740, SENSOR_PRESS.getType(), "pressure1")
    );
    private final List<Metric> metricList = asList(
            new Metric("sensor_e_quantity", 50d, 100d),
            new Metric("sensor_state", 50d, 100d),
            new Metric("sensor_up_time", 50d, 100d),
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
                            .sensorId(component.getSensorId())
                            .metricName(metric.getName())
                            .metricValue(metric.getRandomNumber())
                            .build());
                }
            }
            System.out.println("传感器发送完");
            TimeUnit.MINUTES.sleep(1);
        }
    }
}