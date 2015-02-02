package protobuf;


import client.RaspberryLifeClient;
import event.ModuleEvent;
import system.service.EventBusService;
import util.Log;
import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufInstructionResolver {

    public static final String DEBUG_TAG = ProtobufInstructionResolver.class.getSimpleName();

    public void resolve(RaspberryLifeClient client, RBLMessage message){
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
                break;
        }
    }


    private void runModuleInstruction(RBLMessage message) {
        Log.add(DEBUG_TAG, "Running module instruction");
        RBLMessage.RunInstruction rI = message.getRunInstruction();
        if(rI != null){
            ModuleEvent me = new ModuleEvent();
            me.setType(rI.getInstruction().getModuleType().getNumber() + 1);
            me.setModuleId(rI.getInstruction().getModuleId());
            me.setInstructionId(rI.getInstruction().getInstructionId());
            me.setParameters(rI.getInstruction().getParametersList());
            Log.add(DEBUG_TAG, "MT=" + me.getType()
                    + " MID=" + me.getModuleId()
                    + " IID=" + me.getInstructionId()
                    + " INT_PARAMS=" + me.getParameters());
            EventBusService.post(me);
        }
    }

    private void runClientInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running client instruction");
    }

    private void runSystemInstruction(RBLMessage message){
        Log.add(DEBUG_TAG, "Running system instruction");
    }



}
