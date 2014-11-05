package server;

import client.ClientHandler;
import client.WebSocketClient;
import com.google.protobuf.InvalidProtocolBufferException;
import data.Log;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import protobuf.RBLproto.*;

/**
 * Created by Peter Mösenthin.
 *
 * WebSocketHandler for the webbit webserver. First instance to handle
 * websocket connections.
 */
public class RBLWebSocketHandler extends BaseWebSocketHandler{

    public static final String DEBUG_TAG = "RBLWebSocketHandler";
    private ClientHandler clientHandler = ClientHandler.getInstance();

    @Override
    public void onOpen(WebSocketConnection connection) {
        clientHandler.handleWebSocketClient(connection);
        Log.add(DEBUG_TAG,
                "WebSocketConnection opened. Connection hashcode: "
                        + connection.hashCode());
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        clientHandler.getWebSocketClient(connection)
                .closeConnection();
        Log.add(DEBUG_TAG,
                "WebSocketConnection closed. Connection hashcode: "
                        + connection.hashCode());
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
                WebSocketClient client = clientHandler.getWebSocketClient(connection);
                if(client != null) {
                    client.readMessage(parsedMessage);
                }else {
                    Log.add(DEBUG_TAG,
                            "Unable to pass message to receiving connection. " +
                                    "Client was null.");
                }
        }else {
            Log.add(DEBUG_TAG,
                    "Unable to pass message to receiver connection. " +
                            "Message was null.");
        }
    }


}
