package protobuf;

import client.RaspberryLifeClient;
import data.model.Logic;
import system.service.DataBaseService;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufLogicResolver {

    public static final String DEBUG_TAG = ProtobufLogicResolver.class.getSimpleName();

    private RaspberryLifeClient client;

    public void resolve(RaspberryLifeClient client, RblProto.RBLMessage message){
        this.client = client;
        RblProto.RBLMessage.Logic logic = message.getLogic();

        switch (logic.getCrudType()){
            case CREATE:
                createLogic(logic);
                break;
            case RETRIEVE:
                retrieveLogic(logic);
                break;
            case UPDATE:
                updateLogic(logic);
                break;
            case DELETE:
                deleteLogic(logic);
                break;
        }
    }

    public void createLogic(RblProto.RBLMessage.Logic logic){
        Logic logic_data = new Logic();
/*
        String initiators = "[ ";
        for(RBLMessage.Actuator a : l.getInitiatorList()){
            initiators += a.getName() + "(" + a.getActuatorId() + ") ";
        }
        initiators += "]";

        String receivers = "[ ";
        for(RBLMessage.Actuator a : l.getReceiverList()){
            receivers += a.getName() + "(" + a.getActuatorId() + ") ";
        }
        receivers += "]";

        String conditions = "[ ";
        for(RBLMessage.Condition c : l.getConditionList()){
            conditions += "FID=" + c.getFieldId()
                    + " TU=" + c.getThresholdUnder()
                    + " TO=" + c.getThresholdOver()
                    + " S=" + c.getState();
        }
        conditions += "]";

        String triggers = "[ ";
        for(RBLMessage.Trigger t : l.getTriggerList()){
            triggers += "IID=" + t.getInstructionId() + " "
                        + " PARAMS=( ";
            for(String s : t.getParametersList()){
                triggers += s + " ";
            }
            triggers += ")";
        }
        triggers += "]";

        Log.add(DEBUG_TAG,
                "Received Logic:"
                + " Name: " + l.getName()
                + " Initiators: " + initiators
                + " Conditions: " + conditions
                + " Receivers: " + receivers
                + " Triggers: " + triggers
        );
        */

        DataBaseService.getInstance().writeLogic(logic_data);
    }

    public void retrieveLogic(RblProto.RBLMessage.Logic logic){

    }

    public void updateLogic(RblProto.RBLMessage.Logic logic){

    }

    public void deleteLogic(RblProto.RBLMessage.Logic logic){

    }

}
