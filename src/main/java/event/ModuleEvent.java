package event;

import util.StringUtil;

/**
 * Created by Peter Mösenthin.
 */
public class ModuleEvent extends InstructionEvent {

    private int moduleType;
    private int moduleId;
    private static int messageSequenceNumber = 0;

    public String build(){
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
            serialMessage += ":";
            serialMessage += StringUtil.getZeroPadded(parameters.get(0), 4, true);
        }

        serialMessage += ":";
        serialMessage = StringUtil.getZeroPadded(serialMessage, 32, false);
        return serialMessage;
    }

    private int getMessageSequenceNumber(){
        messageSequenceNumber++;
        if(messageSequenceNumber > 99) {
            messageSequenceNumber = 0;
        }
        return messageSequenceNumber;
    }

    public int getType() {
        return moduleType;
    }

    public void setType(int type) {
        this.moduleType = type;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }


}
