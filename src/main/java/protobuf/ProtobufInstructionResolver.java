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
        for(RBLMessage.RunInstruction ri : message.getRunInstructionList()){
            switch(ri.getActuator().getActuatorType()){
                case MODULE:
                    runModuleInstruction(ri);
                    break;
                case SYSTEM:
                    runSystemInstruction(ri);
                    break;
                case CLIENT:
                    runClientInstruction(ri);
                    break;
            }
        }
    }


    private void runModuleInstruction(RBLMessage.RunInstruction runInstruction) {
        Log.add(DEBUG_TAG, "Running module instruction");
        if(runInstruction != null){
            ModuleEvent me = new ModuleEvent();
            me.setType(runInstruction.getActuator().getModuleType().getNumber() + 1);
            me.setModuleId(runInstruction.getActuator().getModuleId());
            me.setInstructionId(runInstruction.getInstruction().getInstructionId());
            me.setParameters(runInstruction.getInstruction().getParametersList());
            Log.add(DEBUG_TAG, "MT=" + me.getType()
                    + " MID=" + me.getModuleId()
                    + " IID=" + me.getInstructionId()
                    + " INT_PARAMS=" + me.getParameters());
            EventBusService.post(me);
        }
    }

    private void runClientInstruction(RBLMessage.RunInstruction runInstruction){
        Log.add(DEBUG_TAG, "Running client instruction");
        switch(runInstruction.getInstruction().getInstructionId()){
            case 0:
                //TODO notify user
                break;
        }
    }

    private void runSystemInstruction(RBLMessage.RunInstruction runInstruction){
        Log.add(DEBUG_TAG, "Running system instruction");
    }



}
