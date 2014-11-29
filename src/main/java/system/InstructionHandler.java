package system;


import event.EventBusService;
import event.message.ModuleInstruction;
import util.Log;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 */
public class InstructionHandler {

    public static final String DEBUG_TAG = InstructionHandler.class.getSimpleName();

    public void handleRunInstruction(RBLMessage message){
        RBLMessage.ActuatorType type = message.getRunInstruction().getActuator().getActuatorType();
        switch(type){
            case MODULE:
                runModuleInstruction(message);
                break;
            case SYSTEM:
                runSystemInstruction(message);
                break;
            case CLIENT:
                runClientInstruction(message);
        }
    }


    public void runModuleInstruction(RBLMessage message) {
        Log.add(DEBUG_TAG, "Running module instruction");
        RBLMessage.RunInstruction rI = message.getRunInstruction();
        if(rI != null){
            ModuleInstruction mi = new ModuleInstruction();
            mi.setType(rI.getInstruction().getModuleType().getNumber());
            mi.setModuleId(rI.getInstruction().getModuleId());
            mi.setInstructionId(rI.getInstruction().getInstructionId());
            mi.setIntParameters(rI.getInstruction().getIntParametersList());
            Log.add(DEBUG_TAG, "MT=" + mi.getType()
                    + " MID=" + mi.getModuleId()
                    + " IID=" + mi.getInstructionId()
                    + " INT_PARAMS=" + mi.getIntParameters());
            EventBusService.post(mi);
        }
    }

    public void runClientInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running client instruction");
    }

    public void runSystemInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running system instruction");
    }



}
