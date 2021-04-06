package task;

import bean.HeatData;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static bean.MetricType.SUPPLY_PIPE;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Pipe extends MetricTask {
    private final LinkedHashMap<Double, Double> position = new LinkedHashMap<Double, Double>() {{
        put( 113.60017, 34.74740 );
        put( 113.59426, 34.74733 );
        put( 113.59131, 34.74740 );
        put( 113.58899, 34.74692 );
        put( 113.58491, 34.74740 );
    }};
    private final String[] tubeMetricNameArr = {
            "pipeline_water_temperature",
            "pipeline_water_flow",
            "pipeline_water_pressure",
            "pipeline_water_tassels",
            "pipeline_water_temperature_increase",
            "pipeline_water_pressure_increase",
            "pipeline_water_level",
            "pipeline_out_temperature",
            "pipeline_valve_state"};

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            position.forEach( (lon, lat) -> {
                for (String item : tubeMetricNameArr) {
                    HeatData data = HeatData.builder()
                            .time( new Date() )
                            .lon( lon )
                            .lat( lat )
                            .type( SUPPLY_PIPE.getType() )
                            .metricName( item )
                            .metricValue( Math.abs( new Random().nextDouble() * 9999 % 100 ) )
                            .build();
                    channel.write( JSON.toJSONString( data ) );
                    channel.writeAndFlush( "$" );
                }
            } );

            TimeUnit.MINUTES.sleep( 1 );
        }
    }
}
