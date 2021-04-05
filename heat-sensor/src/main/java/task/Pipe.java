package task;

import bean.HeatData;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Pipe extends MetricTask {

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
            LinkedList<HeatData> list = new LinkedList<>();
            for (String item : tubeMetricNameArr) {
                HeatData data = HeatData.builder()
                        .time( new Date() )
                        .metricName( item )
                        .metricValue( Math.abs( new Random().nextDouble() * 9999 % 100 ) )
                        .build();
                list.add( data );
            }
            channel.writeAndFlush( JSON.toJSONString( list ) );
            TimeUnit.MINUTES.sleep( 1 );
        }
    }
}
