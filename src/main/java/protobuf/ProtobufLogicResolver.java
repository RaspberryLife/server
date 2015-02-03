package protobuf;

import client.RaspberryLifeClient;
import data.Config;
import data.model.*;
import system.service.DataBaseService;
import util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class ProtobufLogicResolver {

    public static final String DEBUG_TAG = ProtobufLogicResolver.class.getSimpleName();

    private RaspberryLifeClient client;

    public void resolve(RaspberryLifeClient client, RblProto.RBLMessage message){
        this.client = client;
        for(RblProto.RBLMessage.Logic logic : message.getLogicList()){
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
    }


    public void createLogic(RblProto.RBLMessage.Logic logic){
        Logic logic_data = new Logic();
        logic_data.setName(logic.getName());
        logic_data.setExecution_frequency(mapModelExecutionFrequency(logic.getExeFrequency()));
        mapModelExecutionRequirement(logic.getExeRequirement(), logic_data);
        logic_data.setLogic_initiator(mapModelLogicInitiatorList(logic.getLogicInitiatorList()));
        logic_data.setLogic_receiver(mapModelLogicReceiverList(logic.getLogicReceiverList()));

        DataBaseService.getInstance().writeLogic(logic_data);
        //TODO check if insert was successful and respond to client
        RblProto.RBLMessage m = ProtoFactory.buildPlainTextMessage(
                Config.getConf().getString("server.id"),
                RblProto.RBLMessage.MessageFlag.RESPONSE,
                ProtoFactory.buildPlainText("Database entry created")
        );
        client.sendMessage(m);
    }



    public void retrieveLogic(RblProto.RBLMessage.Logic logic){
        List<Logic> ll = DataBaseService.getInstance().readAllLogic();
        List<RblProto.RBLMessage.Logic> logics = new ArrayList<RblProto.RBLMessage.Logic>();
        for(Logic l : ll){
            RblProto.RBLMessage.Logic.Builder logic_i = ProtoFactory.buildLogic(
                    RblProto.RBLMessage.CrudType.RETRIEVE,
                    logic.getId(),
                    l.getName(),
                    mapProtobufLogicInitiatorList(l.getLogic_initiator()),
                    mapProtobufLogicReceiverList(l.getLogic_receiver()),
                    mapProtobufExecutionFrequency(l.getExecution_frequency()).build(),
                    mapProtobufExecutionRequirement(l.getExecution_requirement()));
            logics.add(logic_i.build());
        }
        RblProto.RBLMessage message = ProtoFactory.buildLogicMessage(
                Config.getConf().getString("server.id"),
                RblProto.RBLMessage.MessageFlag.RESPONSE,
                logics);
        client.sendMessage(message);
    }

    public void updateLogic(RblProto.RBLMessage.Logic logic){

    }

    public void deleteLogic(RblProto.RBLMessage.Logic logic){

    }


    //----------------------------------------------------------------------------------------------
    //                                      MAPPINGS (PROTOBUF TO DATABASE MODEL)
    //----------------------------------------------------------------------------------------------

    private void mapModelExecutionRequirement(RblProto.RBLMessage.ExecutionRequirement executionRequirement,
                                              Logic logic){
        switch (executionRequirement){
            case SINGLE:
                logic.setExecution_requirement(Logic.EXECUTION_REQUIREMENT_SINGLE);
                break;
            case MAJORITY:
                logic.setExecution_requirement(Logic.EXECUTION_REQUIREMENT_MAJORITY);
                break;
            case ALL:
                logic.setExecution_requirement(Logic.EXECUTION_REQUIREMENT_SINGLE);
                break;
        }
    }

    /**
     * Maps an actuator from protobuf to the database model.
     * @param actuator
     * @return
     */
    private Actuator mapModelActuator(RblProto.RBLMessage.Actuator actuator){
        Actuator a = new Actuator();
        switch (actuator.getActuatorType()){
            case SYSTEM:
                a.setType(Actuator.TYPE_SYSTEM);
                break;
            case CLIENT:
                a.setType(Actuator.TYPE_CLIENT);
                break;
            case MODULE:
                a.setType(Actuator.TYPE_MODULE);
                break;
        }
        a.setName(actuator.getName());
        return a;
    }

    /**
     * Maps a condition from protobuf to the database model.
     * @param condition
     * @return
     */
    private Condition mapModelCondition(RblProto.RBLMessage.Condition condition){
        Condition c = new Condition();
        c.setField_id(condition.getFieldId());
        c.setThreshold_over(condition.getThresholdOver());
        c.setThreshold_under(condition.getThresholdUnder());
        c.setState(condition.getState());
        return c;
    }

    /**
     * Maps an instruction from protobuf to the database model.
     * Converts parameters as [p,p,p,p,..]
     * @param instruction
     * @return
     */
    private Instruction mapModelInstruction(RblProto.RBLMessage.Instruction instruction){
        Instruction i = new Instruction();
        i.setField_id(instruction.getInstructionId());
        if(instruction.getParametersList().size() > 0){
            String params = "[";
            for (String p : instruction.getParametersList()){
                params += p + ",";
            }
            params = params.substring(0, params.length()-1) + "]";
            i.setParameters(params);
        }
        return i;
    }

    /**
     * Maps a list of logic_initiators from protobuf to the database model.
     * @param logicInitiators
     * @return
     */
    private List<LogicInitiator> mapModelLogicInitiatorList(List<RblProto.RBLMessage.LogicInitiator> logicInitiators){
        List<LogicInitiator> li_list = new ArrayList<LogicInitiator>();
        for(RblProto.RBLMessage.LogicInitiator l : logicInitiators){
            LogicInitiator li = new LogicInitiator();
            li.setActuator(mapModelActuator(l.getInitiator()));
            li.setCondition(mapModelCondition(l.getCondition()));
            li_list.add(li);
        }
        return  li_list;
    }

    /**
     * Maps a list of logic_receivers from protobuf to the database model.
     * @param logicReceivers
     * @return
     */
    private List<LogicReceiver> mapModelLogicReceiverList(List<RblProto.RBLMessage.LogicReceiver> logicReceivers){
        List<LogicReceiver> lr_list = new ArrayList<LogicReceiver>();
        for(RblProto.RBLMessage.LogicReceiver l : logicReceivers){
            LogicReceiver lr = new LogicReceiver();
            lr.setActuator(mapModelActuator(l.getReceiver()));
            lr.setInstruction(mapModelInstruction(l.getInstruction()));
            lr_list.add(lr);
        }
        return  lr_list;
    }

    /**
     * Maps an execution_frequency from protobuf to the database model.
     * @param executionFrequency
     * @return
     */
    private ExecutionFrequency mapModelExecutionFrequency(RblProto.RBLMessage.ExecutionFrequency executionFrequency){
        ExecutionFrequency ef = new ExecutionFrequency();
        switch (executionFrequency.getExeType()){
            case IMMEDIATELY:
                ef.setType(ExecutionFrequency.TYPE_IMMEDIATELY);
                break;
            case MINUTELY:
                ef.setType(ExecutionFrequency.TYPE_MINUTELY);
                break;
            case HOURLY:
                ef.setType(ExecutionFrequency.TYPE_HOURLY);
                break;
            case DAILY:
                ef.setType(ExecutionFrequency.TYPE_DAILY);
                break;
            case WEEKLY:
                ef.setType(ExecutionFrequency.TYPE_WEEKLY);
                break;
            case MONTHLY:
                ef.setType(ExecutionFrequency.TYPE_MONTHLY);
                break;
        }
        if(executionFrequency.hasMinute()){
            ef.setMinute(executionFrequency.getMinute());
        }
        if(executionFrequency.hasHour()){
            ef.setHour(executionFrequency.getHour());
        }
        if(executionFrequency.hasDay()){
            ef.setDay(executionFrequency.getDay());
        }
        if(executionFrequency.hasWeek()){
            ef.setWeek(executionFrequency.getWeek());
        }
        return ef;
    }


    //----------------------------------------------------------------------------------------------
    //                                      MAPPINGS (DATABASE MODEL to PROTOBUF)
    //----------------------------------------------------------------------------------------------

    /**
     * Maps an actuator from the database model to protobuf.
     * @param actuator
     * @return
     */
    private RblProto.RBLMessage.Actuator.Builder mapProtobufActuator(Actuator actuator){
        RblProto.RBLMessage.ActuatorType type = null;
        switch (actuator.getType()){
            case Actuator.TYPE_SYSTEM:
                type = RblProto.RBLMessage.ActuatorType.SYSTEM;
                break;
            case Actuator.TYPE_CLIENT:
                type = RblProto.RBLMessage.ActuatorType.CLIENT;
                break;
            case Actuator.TYPE_MODULE:
                type = RblProto.RBLMessage.ActuatorType.MODULE;
                break;
        }
        return ProtoFactory.buildActuator(type ,actuator.getActuator_id(),actuator.getName());
    }

    /**
     * Maps a condition from the database model to protobuf.
     * @param condition
     * @return
     */
    private RblProto.RBLMessage.Condition.Builder mapProtobufCondition(Condition condition){
        return ProtoFactory.buildCondition(condition.getField_id(),
                condition.getThreshold_over(),
                condition.getThreshold_under(),
                condition.isState());
    }


    /**
     * Maps an instruction from the database model to protobuf.
     * Splits parameters according to [p,p,p,p,...]
     * @param instruction
     * @return
     */
    private RblProto.RBLMessage.Instruction.Builder mapProtobufInstruction(Instruction instruction){
        String params = instruction.getParameters();
        List<String> paramList = new ArrayList<String>();
        if(params != null && !params.isEmpty()){
            params = params.substring(1, params.length()-1);
            String[] split = params.split(",");
            paramList.addAll(Arrays.asList(split));
        }
        return ProtoFactory.buildInstruction(instruction.getField_id(), paramList);
    }

    /**
     * Maps a list of logic_initiators from the database model to protobuf.
     * @param logicInitiators
     * @return
     */
    private List<RblProto.RBLMessage.LogicInitiator>
    mapProtobufLogicInitiatorList(List<LogicInitiator> logicInitiators){
        List<RblProto.RBLMessage.LogicInitiator> li_list = new ArrayList<RblProto.RBLMessage.LogicInitiator>();
        for(LogicInitiator l : logicInitiators){
            RblProto.RBLMessage.LogicInitiator .Builder raw_li
                    = ProtoFactory.buildLogicInitiator(
                    mapProtobufActuator(l.getActuator()),
                    mapProtobufCondition(l.getCondition()));
            li_list.add(raw_li.build());
        }
        return  li_list;
    }

    /**
     * Maps a list of logic_receivers from the database model to protobuf.
     * @param logicReceivers
     * @return
     */
    private List<RblProto.RBLMessage.LogicReceiver> mapProtobufLogicReceiverList(List<LogicReceiver> logicReceivers){
        List<RblProto.RBLMessage.LogicReceiver> lr_list = new ArrayList<RblProto.RBLMessage.LogicReceiver>();
        for(LogicReceiver l : logicReceivers){
            RblProto.RBLMessage.LogicReceiver .Builder raw_lr
                    = ProtoFactory.buildLogicReceiver(
                    mapProtobufActuator(l.getActuator()),
                    mapProtobufInstruction(l.getInstruction()));
            lr_list.add(raw_lr.build());
        }
        return  lr_list;
    }


    private RblProto.RBLMessage.ExecutionFrequency.Builder
    mapProtobufExecutionFrequency(ExecutionFrequency executionFrequency){
        RblProto.RBLMessage.ExecutionType type = null;


        switch (executionFrequency.getType()){
            case ExecutionFrequency.TYPE_IMMEDIATELY:
                type = RblProto.RBLMessage.ExecutionType.IMMEDIATELY;
                break;
            case ExecutionFrequency.TYPE_MINUTELY:
                type = RblProto.RBLMessage.ExecutionType.MINUTELY;
                break;
            case ExecutionFrequency.TYPE_HOURLY:
                type = RblProto.RBLMessage.ExecutionType.HOURLY;
                break;
            case ExecutionFrequency.TYPE_DAILY:
                type = RblProto.RBLMessage.ExecutionType.DAILY;
                break;
            case ExecutionFrequency.TYPE_WEEKLY:
                type = RblProto.RBLMessage.ExecutionType.WEEKLY;
                break;
            case ExecutionFrequency.TYPE_MONTHLY:
                type = RblProto.RBLMessage.ExecutionType.MONTHLY;
                break;
        }

        return ProtoFactory.buildExecutionFrequency(type,
                executionFrequency.getMinute(),
                executionFrequency.getHour(),
                executionFrequency.getDay(),
                executionFrequency.getWeek());

    }

    private RblProto.RBLMessage.ExecutionRequirement mapProtobufExecutionRequirement(String executionRequirement){
        RblProto.RBLMessage.ExecutionRequirement requirement = null;
        switch (executionRequirement){
            case Logic.EXECUTION_REQUIREMENT_SINGLE:
                requirement = RblProto.RBLMessage.ExecutionRequirement.SINGLE;
                break;
            case Logic.EXECUTION_REQUIREMENT_MAJORITY:
                requirement = RblProto.RBLMessage.ExecutionRequirement.MAJORITY;
                break;
            case Logic.EXECUTION_REQUIREMENT_ALL:
                requirement = RblProto.RBLMessage.ExecutionRequirement.ALL;
                break;
        }
        return requirement;
    }
}
