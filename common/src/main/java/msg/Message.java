package msg;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message<T>{
    boolean state;
    int code;
    T body;
}
