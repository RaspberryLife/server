package event.message;

import util.StringUtil;

import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class ModuleInstruction {

    public int moduleType;
    public int moduleId;
    private static int messageSeq = 0;
    public int instructionId;
    public List<Integer> params;


    public String build(){
        String serialMessage = "";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleType), 3, true);

        //Target module id
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(moduleId), 2, true);

        // Message number
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(getMessageSeq()), 2, true);

        // Instruction id
        serialMessage += ":";
        serialMessage += StringUtil.getZeroPadded(String.valueOf(instructionId), 2, true);

        // Parameters
        if(params != null && params.size() > 0){
            serialMessage += ":";
            serialMessage += StringUtil.getZeroPadded(String.valueOf(params.get(0)), 4, true);
        }

        serialMessage += ":";
        serialMessage = StringUtil.getZeroPadded(serialMessage, 32, false);
        return serialMessage;
    }

    private int getMessageSeq(){
        messageSeq++;
        if(messageSeq > 99) {
            messageSeq = 0;
        }
        return messageSeq;
    }


}
