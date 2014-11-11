package message;

import client.RaspberryLifeClient;
import data.Log;
import data.SerialConnector;
import protobuf.RblProto.*;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle serial messages.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

    private static int messageSeq = 0;

    public void handleModuleRunInstruction(RaspberryLifeClient client, RBLMessage message) {
        Log.add(DEBUG_TAG, "Sending module instruction");
        RBLMessage.RunInstruction rI = message.getRunInstruction();
        String serialMessage = "";
        if(rI != null){
            int modelType = rI.getModeltype().getNumber();
            int targetModuleId = rI.getTargetModulID();
            int instructionId = rI.getInstruction().getInstructionID();
            List<Integer> intParams = rI.getInstruction().getIntParametersList();
            List<String> stringParams = rI.getInstruction().getStringParametersList();

            // Model type
            serialMessage += getZeroPaddedString(String.valueOf(modelType), 3, true);

            //Target module id
            serialMessage += ":";
            serialMessage += getZeroPaddedString(String.valueOf(targetModuleId), 2, true);

            // Message number
            serialMessage += ":";
            serialMessage += getZeroPaddedString(String.valueOf(getMessageSeq()), 2, true);

            // Instruction id
            serialMessage += ":";
            serialMessage += getZeroPaddedString(String.valueOf(instructionId), 2, true);

            // Parameters
            if(intParams != null && intParams.size() > 0){
                serialMessage += ":";
                serialMessage += getZeroPaddedString(String.valueOf(intParams.get(0)), 4, true);
            }

            serialMessage = getZeroPaddedString(serialMessage, 32, false);

            sendSerialMessage(serialMessage);
        }
    }

    private void sendSerialMessage(String message){
        Log.add(DEBUG_TAG, "Sending serial message " + message);
        SerialConnector.send(message);
    }

    private int getMessageSeq(){
        messageSeq++;
        if(messageSeq > 99) {
            messageSeq = 0;
        }
        return messageSeq;
    }

    private String getZeroPaddedString(String content, int maxLength, boolean padLeft){
        int paddingLeft = maxLength - content.length();
        String padded = "";
        for(int i = 0; i < paddingLeft; i++){
            padded += "0";
        }
        if(padLeft){
            padded += content;
        } else{
            padded = content + padded;
        }
        return padded;
    }
}
