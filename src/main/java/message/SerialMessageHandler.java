package message;

import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import util.Config;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

    @Subscribe
    public void handleSerialMessage(SerialMessage message) {
        Log.add(DEBUG_TAG, "Received message event");
        ClientHandler.broadcastMessage(
                ProtoFactory.buildPlainTextMessage(
                        Config.getConf().getString("server.id"),
                        RblProto.RBLMessage.MessageFlag.RESPONSE,
                        "Serial connector received message: " +
                                message
                ));
    }
}
