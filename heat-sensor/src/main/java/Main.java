import client.Client;
import task.Pipe;
import task.Pump;
import task.Station;

import static java.util.Arrays.asList;

/**
 * @author haya
 */
public class Main {
    public static void main(String[] args){
        Client client = new Client();
        client.start("127.0.0.1", 9616, asList(
                new Pipe(),
                new Station(),
                new Pump()
        ));
    }
}
