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
public class PumpSensor extends Thread {
    private final String sensorNamePrefix = "pump";
    private final int type = ComponetType.PummpSensor.getType();
    private final BlockingQueue<HeatData> queue = Common.getQueue();
    private final List<Metric> metricList = asList(
            Metric.builder().name( pump_watt.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( pump_voltage.name() ).min( 0d ).max( 5d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( pump_state.name() ).min( 0d ).max( 3d ).sensorId( sensorNamePrefix ).build(),

            Metric.builder().name( sensor_e_quantity.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( sensor_state.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( sensor_up_time.name() ).min( 50d ).max( 100d ).sensorId( sensorNamePrefix ).build(),
            Metric.builder().name( component_up.name() ).min( 0D ).max( 0d ).sensorId( sensorNamePrefix ).build()
    );
    private final List<Component> componentList = asList(

            Component.builder().lon( 113.599917 ).lat( 34.752758 ).type( type ).sensorSuffix( 1 ).build()
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
                            .metricName( metric.getName() )
                            .metricValue( metric.getRandomNumber() )
                            .build() );
                }
            }
            TimeUnit.MINUTES.sleep( 1 );

        }
    }

}
