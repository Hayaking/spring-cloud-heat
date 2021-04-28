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
            Component.builder().lon(113.60017).lat(34.74740).type(SUPPLY_PIPE.getType()).sensorSuffix(1).build(),
            Component.builder().lon(113.59426).lat(34.74733).type(BACK_PIPE.getType()).sensorSuffix(2).build(),
            Component.builder().lon(113.59131).lat(34.74740).type(SUPPLY_SECONDARY_PIPE.getType()).sensorSuffix(3).build(),
            Component.builder().lon(113.58899).lat(34.74692).type(BACK_SECONDARY_PIPE.getType()).sensorSuffix(4).build(),
            Component.builder().lon(113.58491).lat(34.74740).type(CIRCULATION_PIPE.getType()).sensorSuffix(5).build()
    );

    private final List<Metric> metricList = asList(
            Metric.builder().name("pipeline_water_temperature").min(50d).max(100d).sensorId("temperature").build(),
            Metric.builder().name("pipeline_water_flow").min(50d).max(100d).sensorId("flow").build(),
            Metric.builder().name("pipeline_water_pressure").min(50d).max(100d).sensorId("pressure").build(),
            Metric.builder().name("pipeline_water_tassels").min(50d).max(100d).sensorId("flow").build(),
            Metric.builder().name("pipeline_water_temperature_increase").min(50d).max(100d).sensorId("temperature").build(),
            Metric.builder().name("pipeline_water_pressure_increase").min(50d).max(100d).sensorId("pressure").build(),
            Metric.builder().name("pipeline_water_level").min(50d).max(100d).sensorId("flow").build(),
            Metric.builder().name("pipeline_out_temperature").min(50d).max(100d).sensorId("temperature_out").build(),
            Metric.builder().name("pipeline_valve_state").min(0d).max(3d).sensorId("valve").clazz(Integer.class).build(),
            Metric.builder().name("component_up").min(0d).max(0d).clazz(Integer.class).build()
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
                            .sensorId(metric.getSensorId() + component.getSensorSuffix())
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
