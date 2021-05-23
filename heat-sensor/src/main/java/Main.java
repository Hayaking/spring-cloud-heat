import bean.ServerSocket;
import client.Client;
import sensor.*;

/**
 * @author haya
 */
public class Main {
    private static final ServerSocket SERVER = new ServerSocket( "127.0.0.1", 7070 );

    public static void main(String[] args) {
        startFlowSensor();
        startPressureSensor();
        startTemperatureSensor();
        startValveSensor();
        startPumpSensor();
    }

    public static void startPressureSensor() {
        new Client().start( 7085, SERVER, new PipePressureSensor( 113.59561, 34.752687 ) );
        new Client().start( 7086, SERVER, new PipePressureSensor( 113.608024, 34.747923 ) );
        new Client().start( 7087, SERVER, new PipePressureSensor( 113.608886,34.732914 ) );
    }

    public static void startFlowSensor() {
        new Client().start( 7080, SERVER, new PipeFlowSensor( 113.594424, 34.752732 ) );
    }

    public static void startTemperatureSensor() {
        new Client().start( 7090, SERVER, new PipeTemperatureSensor( 113.597051, 34.752613 ) );
        new Client().start( 7091, SERVER, new PipeTemperatureSensor( 113.608150, 34.746841 ) );
    }

    public static void startValveSensor() {
        new Client().start( 7095, SERVER, new PipeValveSensor( 113.598996, 34.752732 ) );
        new Client().start( 7096, SERVER, new PipeValveSensor( 113.598996, 34.752732 ) );
    }

    public static void startPumpSensor() {
        new Client().start( 7100, SERVER, new PumpSensor( 113.599917, 34.752758 ) );
        new Client().start( 7101, SERVER, new PumpSensor( 113.608222, 34.744245 ) );
    }
}
