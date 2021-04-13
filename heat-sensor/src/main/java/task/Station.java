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

import static bean.MetricType.STATION;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Station extends MetricTask {
    private final LinkedHashMap<Double, Double> position = new LinkedHashMap<Double, Double>() {{
        put(113.63986, 34.72427);
        put(113.63186, 34.71207);
    }};
    private final String[] tubeMetricNameArr = {
            "station_water_temperature",
            "station_water_flow",
            "station_water_pressure",
            "station_water_tassels",
            "station_temperature_increase",
            "station_water_pressure_increase",
            "station_temperature",
            "station_valve_state"
    };

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            position.forEach((lon, lat) -> {
                for (String item : tubeMetricNameArr) {
                    HeatData data = HeatData.builder()
                            .time(new Date())
                            .lon(lon)
                            .lat(lat)
                            .type(STATION.getType())
                            .metricName(item)
                            .metricValue(Math.abs(new Random().nextDouble() * 9999 % 100))
                            .build();
                    channel.write(JSON.toJSONString(data));
                    channel.writeAndFlush("$");
                }
            });

            TimeUnit.MINUTES.sleep(1);
        }
    }
}