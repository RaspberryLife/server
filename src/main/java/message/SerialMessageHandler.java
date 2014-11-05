package message;

import client.RaspberryLifeClient;
import data.Log;
import data.SerialConnector;
import protobuf.ProtoFactory;
import util.Config;
import protobuf.RBLproto.*;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle serial messages.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = "SerialMessageHandler";

    /**
     * Handles instructions for modules in form of strings
     * A instruction is defined as follows:
     * command0:command1:command2
     * @param instruction
     */
    public void handleModuleInstruction(RaspberryLifeClient client, String instruction){
        Log.add(DEBUG_TAG, "Received module instruction");
        String[] commands = instruction.split(":");
        RBLMessage serialFailed = ProtoFactory.buildPlainTextMessage(Config
                .SERVER_ID, "Could not deliver instrction.");
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
