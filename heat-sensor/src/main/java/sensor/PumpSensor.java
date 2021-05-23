package sensor;

import bean.ComponetType;
import bean.HeatData;
import bean.Metric;
import config.Common;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import static bean.MetricName.*;
import static java.util.Arrays.asList;

/**
 * @author haya
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class PumpSensor extends TimerTask {
    private static final int TYPE = ComponetType.PummpSensor.getType();
    private static final BlockingQueue<HeatData> QUEUE = Common.getQueue();
    private static final List<Metric> METRIC_LIST = asList(
            Metric.builder().name( pump_watt.name() ).min( 50d ).max( 100d ).build(),
            Metric.builder().name( pump_voltage.name() ).min( 0d ).max( 5d ).build(),
            Metric.builder().name( pump_state.name() ).min( 0d ).max( 3d ).build(),
            Metric.builder().name( sensor_e_quantity.name() ).min( 50d ).max( 100d ).build(),
            Metric.builder().name( sensor_state.name() ).min( 50d ).max( 100d ).build(),
            Metric.builder().name( sensor_up_time.name() ).min( 50d ).max( 100d ).build(),
            Metric.builder().name( component_up.name() ).min( 0D ).max( 0d ).build()
    );

    private double lon;
    private double lat;

    @SneakyThrows
    @Override
    public void run() {
            for (Metric metric : METRIC_LIST) {
                QUEUE.add( HeatData.builder()
                        .time( new Date() )
                        .lon( lon )
                        .lat( lat )
                        .type( TYPE )
                        .metricName( metric.getName() )
                        .metricValue( metric.getRandomNumber() )
                        .build() );
            }
            log.info( "PumpSensor done" );
    }

}
