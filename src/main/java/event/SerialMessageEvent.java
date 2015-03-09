package event;

import util.Log;
import util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageEvent {

    public static final String DEBUG_TAG = SerialMessageEvent.class.getSimpleName();
    
    public boolean sendSerial;

    private static int messageSequenceNumber = 0;

    public String rawContent;

    public int moduleType;
    public int moduleId;
    public int messageSeq;
    public int instructionId;
    public List<String> parameters;

    public SerialMessageEvent(String content){
        this.rawContent = content;
    }

    public void buildModel(){
        parameters = new ArrayList<String>();
        try {
            String[] split = rawContent.split(":");
            moduleType = Integer.parseInt(split[0]);
            moduleId = Integer.parseInt(split[1]);
            messageSeq = Integer.parseInt(split[2]);
            instructionId = Integer.parseInt(split[3]);

            for(int i = 4; i < split.length; i++){
                parameters.add(split[i]);
            }
        } catch (Exception e){
            Log.add(DEBUG_TAG, "Unable to populate serial message", e);
        }
    }

    public void buildSerial(){
        String serialMessage = "";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleType), 3, true);

        //Target module id
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleId), 2, true);

        // Message number
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(getMessageSequenceNumber()),
                2, true);

        // InstructionEvent id
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(instructionId), 2, true);

        // Parameters
        if(parameters != null && parameters.size() > 0){
            for(String p : parameters){
                serialMessage += ":";
                //padding usually set to 4 (now to 2 since reed only ready two chars)
                serialMessage += StringUtil.getZeroPadded(p, 2, true);
            }
        }
        serialMessage += ":";
        serialMessage = StringUtil.getZeroPadded(serialMessage, 32, false);

        rawContent = serialMessage;
    }

    private int getMessageSequenceNumber(){
        messageSequenceNumber++;
        if(messageSequenceNumber > 99) {
            messageSequenceNumber = 0;
        }
        return messageSequenceNumber;
    }

}
