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
public class PipeValveSensor extends TimerTask {
    private static final int TYPE = ComponetType.ValveSensor.getType();
    private static final BlockingQueue<HeatData> QUEUE = Common.getQueue();
    private static final List<Metric> METRIC_LIST = asList(
            Metric.builder().min( 50d ).max( 100d ).build().setMetric( pipeline_valve_state ),
            Metric.builder().min( 50d ).max( 100d ).build().setMetric( sensor_e_quantity ),
            Metric.builder().min( 50d ).max( 100d ).build().setMetric( sensor_state ),
            Metric.builder().min( 50d ).max( 100d ).build().setMetric( sensor_up_time ),
            Metric.builder().min( 0d ).max( 0d ).build().setMetric( component_up )
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
                    .aliasName( metric.getAliasName() )
                    .unit( metric.getUnit() )
                    .metricValue( metric.getRandomNumber() )
                    .build() );
        }
        log.info( "PipeValveSensor done" );
    }
}
