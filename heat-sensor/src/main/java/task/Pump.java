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

import static bean.MetricType.CIRCLE_PUMP;
import static bean.MetricType.MAKE_PUMP;

/**
 * @author haya
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class Pump extends MetricTask {
    private final String[] tubeMetricNameArr = {
            "pump_watt",
            "pump_voltage",
            "pump_state"
    };
    private final LinkedList<Component> components = new LinkedList<Component>() {{
        add(new Component() {{
            setLon(113.63477);
            setLat(34.75035);
            setType(CIRCLE_PUMP.getType());
        }});
        add(new Component() {{
            setLon(113.617440);
            setLat(34.75063);
            setType(MAKE_PUMP.getType());
        }});
    }};

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            for (Component component : components) {
                for (String item : tubeMetricNameArr) {
                    HeatData data = HeatData.builder()
                            .time(new Date())
                            .lon(component.getLon())
                            .lat(component.getLat())
                            .type(component.getType())
                            .metricName(item)
                            .metricValue(Math.abs(new Random().nextDouble() * 9999 % 100))
                            .build();
                    channel.write(JSON.toJSONString(data));
                    channel.writeAndFlush("$");
                }
            }

            TimeUnit.MINUTES.sleep(1);

        }
    }

    @Data
    static class Component {
        double lon;
        double lat;
        int type;
    }
}