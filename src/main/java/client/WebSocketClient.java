package client;

import util.Log;
import org.webbitserver.WebSocketConnection;
import protobuf.ProtobufMessageHandler;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * This is the representaion of a RaspberryHomeClient using a Websocket
 * connection.
 */
public class WebSocketClient extends RaspberryLifeClient {
    public static final String DEBUG_TAG = WebSocketClient.class.getSimpleName();

    private WebSocketConnection connection;
    private final ProtobufMessageHandler messageHandler =
            new ProtobufMessageHandler(this);

    public WebSocketClient(WebSocketConnection connection){
        this.connection = connection;
    }


    //==========================================================================
    // Connection state handling
    //==========================================================================
    @Override
    protected void onConnectionClosed() {
        //TODO implement
    }

    @Override
    protected void onConnectionDenied(String reason) {
        //TODO implement
    }

    @Override
    protected void onConnectionAccepted() {
        //TODO implement
    }

    //==========================================================================
    // Message handling
    //==========================================================================

    @Override
    public void sendMessage(RBLMessage message) {
        if(message != null) {
            connection.send(message.toByteArray());
        }else {
            Log.add(DEBUG_TAG, "Message was empty. ID=" + getId());
        }
    }

    //@Override
    public void readMessage(RBLMessage message){
            messageHandler.handleMessage(message);
    }

    //==========================================================================
    // Getter & Setter
    //==========================================================================


    public WebSocketConnection getWebSocketConnection(){
        return connection;
    }


}
