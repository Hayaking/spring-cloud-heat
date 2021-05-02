package sensor;

import bean.Component;
import bean.ComponetType;
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

import static bean.MetricName.*;
import static java.util.Arrays.asList;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class PipeValveSensor extends Thread {
    private final String sensorNamePrefix = "valve";
    private final int type = ComponetType.ValveSensor.getType();
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Component> componentList = asList(
            Component.builder().lon( 113.598996 ).lat( 34.752732 ).type( type ).sensorSuffix( 1 ).build()
    );

    private final List<Metric> metricList = asList(
            Metric.builder().name( pipeline_valve_state.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),

            Metric.builder().name( sensor_e_quantity.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( sensor_state.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( sensor_up_time.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( component_up.name() ).min( 0d ).max( 0d ).build()
    );

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            for (Component component : componentList) {
                for (Metric metric : metricList) {
                    queue.add( HeatData.builder()
                            .time( new Date() )
                            .lon( component.getLon() )
                            .lat( component.getLat() )
                            .type( component.getType() )
                            .sensorId( metric.getSensorId() + component.getSensorSuffix() )
                            .metricName( metric.getName() )
                            .metricValue( metric.getRandomNumber() )
                            .build() );
                }
            }
            TimeUnit.MINUTES.sleep( 1 );
        }
    }
}
