package protobuf;

import client.RaspberryLifeClient;
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

    public void handleMessage(RBLMessage message){
        if(message.getMessageType().equals(RBLMessage.MessageType.AUTH)){
            handleAuthMessage(message);
        }else {
            if(client.isAccepted) {
                switchType(message);
            } else {
                Log.add(DEBUG_TAG,
                        "Cannot handle message. Client is not accepted"
                                +" ID=" + client.getId()
                );
            }
        }
    }


    private void switchType(RBLMessage message){
        RBLMessage.MessageType type = message.getMessageType();
        switch (type) {
            case AUTH:
                break;
            case PLAIN_TEXT:
                handlePlaintextMessage(message);
                break;
            case RUN_INSTRUCTION:
                handleRunInstruction(message);
                break;
            case DATASET:
                handleDataSetMessage(message);
                break;
            case LOGIC:
                handleLogicMessage(message);
                break;
        }
    }

    private void handleLogicMessage(RBLMessage message) {
        //TODO implement
    }

    private void handleDataSetMessage(RBLMessage message) {
        //TODO implement
    }


    private void handleRunInstruction(RBLMessage message) {
            SystemManager.getInstance().getInstructionHandler()
                    .handleRunInstruction(message);

    }

    private void handlePlaintextMessage(RBLMessage message) {
        String text = message.getPlainText().getText();
        Log.add(DEBUG_TAG, "Client " + client.getId() + " says: " + text);
    }

    private void handleAuthMessage(RBLMessage message) {
        String key = message.getPlainText().getText();
        boolean accepted = NoAuth.verify(key);
        client.setId(message.getId());
        if(accepted){
            client.acceptConnection();
        } else {
            client.denyConnection("Bad key");
        }
    }


}
