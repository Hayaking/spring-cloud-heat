import client.Client;
import task.Pipe;
import task.Pump;
import task.Station;

/**
 * @author haya
 */
public class Main {
    public static void main(String[] args){
        Client client = new Client();
//        client.start( "127.0.0.1", 9616, new Pipe() );
//        client.start( "127.0.0.1", 9616, new Station() );
        client.start("127.0.0.1", 9616, new Pump());
    }
}
