package web;

import client.WebSocketClient;
import com.google.protobuf.InvalidProtocolBufferException;
import data.Log;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;
import protobuf.RBHproto.RBHMessage;
import server.RBHServer;

/**
 * Created by Peter Mösenthin.
 *
 * Websockethandler for the webbit Webserver. First instance to handle
 * websocketconnections.
 */
public class RBHWebSocketHandler extends BaseWebSocketHandler{

    public static final String DEBUG_TAG = "RBHWebSockethandler";

    @Override
    public void onOpen(WebSocketConnection connection) {
        RBHServer.getClientHandler().handleWebSocketClient(connection);
        Log.add(DEBUG_TAG,
                "WebSocketConnection opened. Connection hashcode: "
                        + connection.hashCode());
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        RBHServer.getClientHandler().getWebSocketClient(connection)
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
        RBHMessage parsedMessage = null;
        try {
            parsedMessage = RBHMessage.parseFrom(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        //pass message to receiving client
        if(parsedMessage != null) {
                WebSocketClient client = RBHServer.getClientHandler()
                        .getWebSocketClient(connection);
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
