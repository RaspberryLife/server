package protobuf;


import event.ModuleEvent;
import system.service.EventBusService;
import util.Log;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufInstructionResolver {

    public static final String DEBUG_TAG = ProtobufInstructionResolver.class.getSimpleName();

    public void resolve(RBLMessage message){
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


    private void runModuleInstruction(RBLMessage message) {
        Log.add(DEBUG_TAG, "Running module instruction");
        RBLMessage.RunInstruction rI = message.getRunInstruction();
        if(rI != null){
            ModuleEvent mi = new ModuleEvent();
            mi.setType(rI.getInstruction().getModuleType().getNumber() + 1);
            mi.setModuleId(rI.getInstruction().getModuleId());
            mi.setInstructionId(rI.getInstruction().getInstructionId());
            mi.setParameters(rI.getInstruction().getParametersList());
            Log.add(DEBUG_TAG, "MT=" + mi.getType()
                    + " MID=" + mi.getModuleId()
                    + " IID=" + mi.getInstructionId()
                    + " INT_PARAMS=" + mi.getParameters());
            EventBusService.post(mi);
        }
    }

    private void runClientInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running client instruction");
    }

    private void runSystemInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running system instruction");
    }



}
