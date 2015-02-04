package client;

import data.Config;
import protobuf.ProtoFactory;
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
public class RBLWebSocketClient extends RaspberryLifeClient {

    public static final String DEBUG_TAG = RBLWebSocketClient.class.getSimpleName();

    private WebSocketConnection connection;
    private final ProtobufMessageHandler messageHandler =
            new ProtobufMessageHandler(this);

    public RBLWebSocketClient(WebSocketConnection connection){
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
        RBLMessage m =
                ProtoFactory.buildAuthMessage(Config.getConf().getString("server.id"),
                        RBLMessage.MessageFlag.RESPONSE,
                        ProtoFactory.buildPlainText("Client denied. REASON=" + reason)
                );
        sendMessage(m);
    }

    @Override
    protected void onConnectionAccepted() {
        RBLMessage m =
                ProtoFactory.buildAuthMessage(Config.getConf().getString("server.id"),
                        RBLMessage.MessageFlag.RESPONSE,
                        ProtoFactory.buildPlainText("Accepted client with id: "
                                + getId()
                                + " TIME= " + System.currentTimeMillis())
                );
        sendMessage(m);
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
