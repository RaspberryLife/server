package message;

import client.RaspberryLifeClient;
import data.DataBaseHelper;
import data.Log;
import data.SerialConnector;
import server.NoAuth;
import util.Config;
import message.RBHproto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Handles all incoming messages for a RaspberryHomeClient.
 */
public class ProtobufMessageHandler {

    private RaspberryLifeClient client;
    public static final String DEBUG_TAG = "ProtoBufMessageHandler";
    private SerialMessageHandler serialHandler;

    public ProtobufMessageHandler(RaspberryLifeClient client){
        this.client = client;
    }


    /**
     * Handle the actual RBHMessage.
     * @param message
     */
    public void handleMessage(RBHproto.RBHMessage message){
        // Auth message
        if(message.getMType() == RBHMessage.MessageType.AUTH_REQUEST){
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
        else if(message.getMType() == RBHMessage.MessageType.PLAIN_TEXT) {
            String text = message.getPlainText().getText();
            if(client.isAccepted){
                // Check for instrunction trigger "module"
                if(text.startsWith("module")){
                    if(serialHandler == null){
                        serialHandler = new SerialMessageHandler();
                    }
                    serialHandler.handleModuleInstruction(client, text);
                }
            }
        }

        // GetDataSet message
        else if(message.getMType() == RBHMessage.MessageType.GET_DATA_SET){
            if(client.isAccepted) {
                DataBaseHelper dbh = new DataBaseHelper();
                RBHproto.RBHMessage m =
                        ProtoFactory.buildDataSetMessage(
                                Config.SERVER_ID,
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
