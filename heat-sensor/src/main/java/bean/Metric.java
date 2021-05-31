package bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

import static bean.MetricName.sensor_up_time;

/**
 * @author haya
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Metric {
    private final long START_TIME = System.currentTimeMillis();
    private static final Random RANDOM = new Random();
    private String name;
    private String aliasName;
    private String unit;
    private Double min;
    private Double max;
    private Class clazz = Double.class;
    private String sensorId;

    public Metric(String name, Double min, Double max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public Metric(String name, Double min, Double max, Class clazz) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.clazz = clazz;
    }

    public Metric(String name, Double min, Double max, String sensorId) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.sensorId = sensorId;
    }

    public Double getRandomNumber() {
        if (sensor_up_time.name().equals( name )) {
            return (double) (System.currentTimeMillis() - START_TIME);
        }
        if (clazz == Integer.class) {
            if (max == 0) {
                return 0d;
            }
            return RANDOM.nextInt(max.intValue()) % (max - min) + min;
        } else  {
            return (RANDOM.nextDouble()*10000) % (max - min) + min;
        }

    }

    public Metric setMetric(MetricName metricName) {
        setName( metricName.name() );
        setAliasName( metricName.getAliasName() );
        setUnit( metricName.getUnit() );
        return this;
    }

    public static void main(String[] args) {
        Metric metric = new Metric();
        metric.setClazz(Double.class);
        metric.setMax(100D);
        metric.setMin(50D);
        System.out.println(metric.getRandomNumber());
    }
}
