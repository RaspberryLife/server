package protobuf;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufLogicResolver {

    public static final String DEBUG_TAG = ProtobufLogicResolver.class.getSimpleName();

    public void resolve(RblProto.RBLMessage message){
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

    }

    public void retrieveLogic(RblProto.RBLMessage.Logic logic){

    }

    public void updateLogic(RblProto.RBLMessage.Logic logic){

    }

    public void deleteLogic(RblProto.RBLMessage.Logic logic){

    }

}
