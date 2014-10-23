package protobuf;

import client.RaspberryHomeClient;
import data.DataBaseHelper;
import data.Log;
import data.SerialConnector;
import server.NoAuth;
import util.Config;
import protobuf.RBHproto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Handles all incoming messages for a RaspberryHomeClient.
 */
public class ProtobufMessageHandler {
    private RaspberryHomeClient client;
    public static final String DEBUG_TAG = "ProtoBufMessageHandler";

    public ProtobufMessageHandler(RaspberryHomeClient client){
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
                    handleModuleInstruction(text);
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

    /**
     * Handles instructions for modules in form of strings
     * A instruction is defined as follows:
     * command0:command1:command2
     * @param instruction
     */
    private void handleModuleInstruction(String instruction){
        Log.add(DEBUG_TAG, "Received module instruction");
        String[] commands = instruction.split(":");
        RBHMessage serialFailed = ProtoFactory.buildPlainTextMessage(Config
                .SERVER_ID,"Could not deliver instrction.");
        //Base
        if(commands[0].equalsIgnoreCase("module")){

            // Socket module
            if(commands[1].equalsIgnoreCase("socket")){
                if(commands[2].equalsIgnoreCase("on")){
                    Log.add(DEBUG_TAG, "Turning module:socket on");
                    SerialConnector.send("0\n");
                } else if(commands[2].equalsIgnoreCase("off")){
                    Log.add(DEBUG_TAG, "Turning module:socket off");
                    SerialConnector.send("1\n");
                }else{
                    Log.add(DEBUG_TAG, "No instruction set for module:socket");
                    client.sendMessage(serialFailed);
                }
            // Reed Switch
            }else if(commands[1].equalsIgnoreCase("reed")){
                if(commands[2].equalsIgnoreCase("switch")){
                    Log.add(DEBUG_TAG, "Switching reed");
                    SerialConnector.send("3\n");
                }else {
                    Log.add(DEBUG_TAG, "No instruction set for module:reed");
                    client.sendMessage(serialFailed);
                }
            // Temp request
            } else if(commands[1].equalsIgnoreCase("temp")){
                Log.add(DEBUG_TAG, "Requesting module:temp");
                SerialConnector.send("2\n");
            }

        }
    }
}
