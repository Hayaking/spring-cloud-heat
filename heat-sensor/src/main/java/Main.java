import client.Client;
import task.Pipe;

/**
 * @author haya
 */
public class Main {
    public static void main(String[] args){
        Client client = new Client();
        client.start( "127.0.0.1", 9616, new Pipe() );
    }
}
