package bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author haya
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Component {
    double lon;
    double lat;
    int type;
    List<Integer> childType;

    public Component(double lon, double lat, int type) {
        this.lon = lon;
        this.lat = lat;
        this.type = type;
    }
}
