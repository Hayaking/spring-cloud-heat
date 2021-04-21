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
public class Station extends Thread {
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Component> componentList = asList(
            new Component(113.63986, 34.72427, STATION.getType(), asList(SUPPLY_PIPE.getType(), BACK_PIPE.getType())),
            new Component(113.63186, 34.71207, STATION.getType(), asList(SUPPLY_SECONDARY_PIPE.getType(), BACK_SECONDARY_PIPE.getType()))
    );
    private final List<Metric> metricList = asList(
            new Metric("station_water_temperature", 50d, 100d),
            new Metric("station_water_flow", 50d, 100d),
            new Metric("station_water_pressure", 50d, 100d),
            new Metric("station_water_tassels", 50d, 100d),
            new Metric("station_temperature_increase", 50d, 100d),
            new Metric("station_water_pressure_increase", 50d, 100d),
            new Metric("station_temperature", 50d, 100d),
            new Metric("station_valve_state", 0d, 0d, Integer.class),
            new Metric("component_up", 0d, 0d, Integer.class)
    );

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            for (Component component : componentList) {
                List<Integer> typeList = component.getChildType();
                for (Integer childType : typeList) {
                    for (Metric metric : metricList) {
                        queue.add(HeatData.builder()
                                .time(new Date())
                                .lon(component.getLon())
                                .lat(component.getLat())
                                .type(component.getType())
                                .childType(childType)
                                .metricName(metric.getName())
                                .metricValue(metric.getRandomNumber())
                                .build());
                    }
                }
            }
            System.out.println("热站发送完");
            TimeUnit.MINUTES.sleep(1);
        }
    }
}