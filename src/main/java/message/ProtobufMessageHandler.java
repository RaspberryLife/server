package message;

import client.RaspberryLifeClient;
import system.InstructionHandler;
import system.SystemManager;
import util.Log;
import util.NoAuth;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Handles all incoming messages for a RaspberryHomeClient.
 */
public class ProtobufMessageHandler {

    private RaspberryLifeClient client;
    public static final String DEBUG_TAG = ProtobufMessageHandler.class.getSimpleName();

    public ProtobufMessageHandler(RaspberryLifeClient client){
        this.client = client;
    }

    /**
     * Handle the actual RBLMessage.
     * @param message
     */
    public void handleMessage(RBLMessage message){
        // Auth message
        if(message.getMessageType() == RBLMessage.MessageType.AUTH){
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
        else if(message.getMessageType() == RBLMessage.MessageType.PLAIN_TEXT) {
            String text = message.getPlainText().getText();
            if(client.isAccepted){
                Log.add(DEBUG_TAG, "Client " + client.getId() + " says: " + text);
            }
        }

        // RunInstruction message
        else if(message.getMessageType() == RBLMessage.MessageType.RUN_INSTRUCTION) {
            if(client.isAccepted){
                SystemManager.getInstance().getInstructionHandler()
                        .handleRunInstruction(message);
            }
        }

        // GetDataSet message
        else if(message.getMessageType() == RBLMessage.MessageType.GET_DATA){
            if(client.isAccepted) {
                //TODO implement
            } else {
                Log.add(DEBUG_TAG,
                        "Unable to send message. Client was not accepted"
                                +" ID=" + client.getId()
                );
            }
        }
    }




}
