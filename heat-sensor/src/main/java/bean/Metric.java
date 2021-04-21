package bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * @author haya
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Metric {
    private static final Random RANDOM = new Random();
    private String name;
    private Double min;
    private Double max;
    private Class clazz = Double.class;

    public Metric(String name, Double min, Double max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public Double getRandomNumber() {

        if (clazz == Integer.class) {
            if (max == 0) {
                return 0d;
            }
            return RANDOM.nextInt(max.intValue()) % (max - min) + min;
        } else if (clazz == Double.class) {
            return (RANDOM.nextDouble()*10000) % (max - min) + min;
        }
        return 0D;
    }

    public static void main(String[] args) {
        Metric metric = new Metric();
        metric.setClazz(Double.class);
        metric.setMax(100D);
        metric.setMin(50D);
        System.out.println(metric.getRandomNumber());
    }
}
