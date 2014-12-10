package server.web;

import client.ClientHandler;
import client.RBLWebSocketClient;
import com.google.common.eventbus.Subscribe;
import com.google.protobuf.InvalidProtocolBufferException;
import event.NewClientEvent;
import event.WebSocketEvent;
import event.WebSocketMessageEvent;
import system.service.EventBusService;
import util.Log;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * WebSocketHandler for the webbit webserver. First instance to handle
 * websocket connections.
 */
public class RBLWebSocketHandler extends BaseWebSocketHandler{

    public static final String DEBUG_TAG = RBLWebSocketHandler.class.getSimpleName();

    @Override
    public void onOpen(WebSocketConnection connection) {
        Log.add(DEBUG_TAG,
                "WebSocketConnection opened. Connection hashcode: "
                        + connection.hashCode());
        WebSocketEvent wse = new WebSocketEvent();
        wse.setConnection(connection);
        wse.setType(WebSocketEvent.TYPE_CONNECTION_OPEN);
        EventBusService.post(wse);
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        Log.add(DEBUG_TAG,
                "WebSocketConnection closed. Connection hashcode: "
                        + connection.hashCode());
        WebSocketEvent wse = new WebSocketEvent();
        wse.setConnection(connection);
        wse.setType(WebSocketEvent.TYPE_CONNECTION_CLOSE);
        EventBusService.post(wse);
    }

    @Override
    public void onMessage(WebSocketConnection connection, byte[] message) {
        Log.add(DEBUG_TAG,
                "WebSocketConnection received message. Connection hashcode: "
                        + connection.hashCode());
        RBLMessage parsedMessage = null;
        try {
            parsedMessage = RBLMessage.parseFrom(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        //pass message to receiving client
        if(parsedMessage != null) {
            WebSocketEvent wse = new WebSocketEvent();
            wse.setMessage(parsedMessage);
            wse.setConnection(connection);
            wse.setType(WebSocketEvent.TYPE_MESSAGE);
            EventBusService.post(wse);
        }else {
            Log.add(DEBUG_TAG,
                    "Unable to pass message to connection. " +
                            "Message was null.");
        }

    }

}
