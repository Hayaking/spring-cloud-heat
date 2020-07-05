package com.consumer.consumer.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.security.auth.message.MessageInfo;
import java.util.Arrays;
import java.util.Set;

/**
 * @author haya
 */
@Component
public class HeatDaTaIO {
    @Autowired
    private SocketIOServer socketIOServer;

    @OnEvent( value = "data/test" )
    public void test(SocketIOClient client, AckRequest request, Object data)  {
        System.out.println(data);
        client.sendEvent( "test", "11111111111111111111" );
        socketIOServer.getClient( client.getSessionId() ).sendEvent( "test","test" );

    }

    @OnEvent( value = "join" )
    public void join(SocketIOClient client, AckRequest request, String data)  {
        Set<String> allRooms = client.getAllRooms();
        for (String name : allRooms) {
            client.leaveRoom( name );
        }
        client.joinRoom( data );
        System.out.println("加入room"+data);
    }

    @OnConnect
    public void onConnect (SocketIOClient client){
        client.sendEvent( "test","test" );
        System.out.println(client.getSessionId());
    }

    @KafkaListener(id = "consumer1", topics = "topic.q1")
    public void data(String message) {
        socketIOServer
                .getRoomOperations(message.split( "," )[1] )
                .sendEvent( "data",message );
    }

    @KafkaListener(id = "consumer3", topics = "topic.q3")
    public void avg(String message) {
        socketIOServer
                .getRoomOperations(message.split( "," )[0].substring( 1 ) )
                .sendEvent( "avgData",message.substring( 1,message.length()-1 ) );
    }

}
