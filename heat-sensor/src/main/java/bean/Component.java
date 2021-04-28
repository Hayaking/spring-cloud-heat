package bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author haya
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Component {
    double lon;
    double lat;
    int type;
    String sensorId;
    Integer sensorSuffix;
    List<Integer> childType;

    public Component(double lon, double lat, int type) {
        this.lon = lon;
        this.lat = lat;
        this.type = type;
    }

    public Component(double lon, double lat, int type, List<Integer> childType) {
        this.lon = lon;
        this.lat = lat;
        this.type = type;
        this.childType = childType;
    }

    public Component(double lon, double lat, int type, String sensorId) {
        this.lon = lon;
        this.lat = lat;
        this.type = type;
        this.sensorId = sensorId;
    }
}
