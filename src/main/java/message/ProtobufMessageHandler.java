package message;

import client.RaspberryLifeClient;
import data.DataBaseHelper;
import util.Log;
import protobuf.ProtoFactory;
import util.NoAuth;
import util.Config;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Handles all incoming messages for a RaspberryHomeClient.
 */
public class ProtobufMessageHandler {

    private RaspberryLifeClient client;
    public static final String DEBUG_TAG = ProtobufMessageHandler.class.getSimpleName();
    private SerialMessageHandler serialHandler;

    public ProtobufMessageHandler(RaspberryLifeClient client){
        this.client = client;
    }


    /**
     * Handle the actual RBLMessage.
     * @param message
     */
    public void handleMessage(RBLMessage message){
        // Auth message
        if(message.getMType() == RBLMessage.MessageType.AUTH_REQUEST){
            String key = message.getPlainText().getText();
            boolean accepted = NoAuth.verify(key);
            client.setId(message.getId());
            if(accepted){
                client.acceptConnection();
            } else {
                client.denyConnection("Bad key");
            }
        }

        // Plaintext message
        else if(message.getMType() == RBLMessage.MessageType.PLAIN_TEXT) {
            String text = message.getPlainText().getText();
            if(client.isAccepted){
                Log.add(DEBUG_TAG, "Client " + client.getId() + " says " + text);
            }
        }

        // RunInstruction message
        else if(message.getMType() == RBLMessage.MessageType.RUN_INSTRUCTION) {
            if(client.isAccepted){
                if(serialHandler == null){
                    serialHandler = new SerialMessageHandler();
                }
                serialHandler.sendModuleRunInstruction(client, message);
            }
        }

        // GetDataSet message
        else if(message.getMType() == RBLMessage.MessageType.GET_DATA_SET){
            if(client.isAccepted) {
                DataBaseHelper dbh = new DataBaseHelper();
                RBLMessage m =
                        ProtoFactory.buildDataSetMessage(
                                Config.get().getString("server.id"),
                                "livingroom_sensormodule",
                                "temp",
                                dbh.getDataList(50),
                                ProtoFactory.DATA_TYPE_FLOAT
                        );
                client.sendMessage(m);
            } else {
                Log.add(DEBUG_TAG,
                        "Unable to send message. Client was not accepted"
                                +" ID=" + client.getId()
                );
            }
        }

    }


}
