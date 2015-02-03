package protobuf;

import client.RaspberryLifeClient;
import data.model.User;
import system.service.DataBaseService;
import util.Log;
import util.NoAuth;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Handles all incoming messages for a RaspberryHomeClient.
 */
public class ProtobufMessageHandler {

    public static final String DEBUG_TAG = ProtobufMessageHandler.class.getSimpleName();

    private RaspberryLifeClient client;

    private ProtobufInstructionResolver instructionResolver = new ProtobufInstructionResolver();
    private ProtobufLogicResolver logicResolver = new ProtobufLogicResolver();


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
        Log.add(DEBUG_TAG,message.toString());
        switch (type) {
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
            case USER:
                handleUserMessage(message);
                break;
        }
    }

    private void handleUserMessage(RBLMessage message) {
        RBLMessage.User user = message.getUser(0);
        User u = new User();
        u.setEmail(user.getEmail());
        u.setName(user.getName());
        u.setId(user.getId());
        DataBaseService.getInstance().insert(u);
    }

    private void handleLogicMessage(RBLMessage message) {
        logicResolver.resolve(client, message);
    }

    private void handleDataSetMessage(RBLMessage message) {
        for(RBLMessage.DataSet d: message.getDataSetList()){
            Log.add(DEBUG_TAG,
                    "Received DataSet:"
                            + " CrudType: " + d.getCrudType()
                            + " DataType: " + d.getDataType()
                            + " Actuator: " + d.getActuator()
                            + " FieldId: " + d.getFieldId()
                            + " Range: "
                            + " Start=" + d.getRange().getStartDateTime()
                            + " End=" + d.getRange().getEndDateTime()
                            + " Count=" + d.getRange().getCount()
            );
        }
    }

    private void handleRunInstruction(RBLMessage message) {
        instructionResolver.resolve(client, message);
    }

    private void handlePlaintextMessage(RBLMessage message) {
        for(RBLMessage.PlainText plainText : message.getPlainTextList()){
            Log.add(DEBUG_TAG, "Client " + client.getId() + " says: " + plainText.getText());
        }
    }

    private void handleAuthMessage(RBLMessage message) {
        String key = message.getPlainTextList().get(0).getText();
        boolean accepted = NoAuth.verify(key);
        client.setId(message.getId());
        if(accepted){
            client.acceptConnection();
        } else {
            client.denyConnection("Bad key");
        }
    }


}
