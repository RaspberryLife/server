package protobuf;

import java.util.Iterator;
import java.util.List;

import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * The ProtoFactory was created for easy access to Protobuf messages
 * to even further reduce code to send messages.
 */
public class ProtoFactory {

    //----------------------------------------------------------------------------------------------
    //                                      BASE
    //----------------------------------------------------------------------------------------------

    private static RBLMessage.Builder createBaseMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.MessageType messageType){
        return RBLMessage.newBuilder()
                .setId(id)
                .setMessageType(messageType)
                .setMessageFlag(messageFlag);
    }

    //----------------------------------------------------------------------------------------------
    //                                      Plaintext
    //----------------------------------------------------------------------------------------------

    public static RBLMessage buildPlainTextMessage(
            String id,
            RBLMessage.MessageFlag flag,
            String plainText){
        return createBaseMessage(id, flag, RBLMessage.MessageType.PLAIN_TEXT)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                                .setText(plainText))
                .build();
    }

    //----------------------------------------------------------------------------------------------
    //                                      INSTRUCTION
    //----------------------------------------------------------------------------------------------

    public static RBLMessage.Instruction.Builder buildInstructionMessage(
            int instructionId,
            List<String> stringParameters){
        RBLMessage.Instruction.Builder instruction = RBLMessage.Instruction.newBuilder();
        instruction.setInstructionId(instructionId);
        if(stringParameters != null){
            instruction.addAllParameters(stringParameters);
        }

        return instruction;
    }

    public static RBLMessage buildRunInstructionMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.Actuator actuator,
            RBLMessage.Instruction.Builder instruction){
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.RUN_INSTRUCTION)
                .setRunInstruction(RBLMessage.RunInstruction.newBuilder()
                                .setActuator(actuator)
                                .setInstruction(instruction)
                ).build();
    }

    //----------------------------------------------------------------------------------------------
    //                                      LOGIC
    //----------------------------------------------------------------------------------------------

    public static RBLMessage.Actuator buildActuator(
            RBLMessage.ActuatorType actuatorType,
            int actuatorId,
            String name){

        RBLMessage.Actuator.Builder actuator =
                RBLMessage.Actuator.newBuilder()
                .setActuatorId(actuatorId)
                .setActuatorType(actuatorType);
        if(name != null && !name.isEmpty()){
            actuator.setName(name);
        }

        return actuator.build();
    }

    public static RBLMessage.Condition buildCondition(
            int fieldId,
            int thresholdOver,
            int thresholdUnder,
            boolean state){
        return RBLMessage.Condition.newBuilder()
                .setFieldId(fieldId)
                .setState(state)
                .setThresholdOver(thresholdOver)
                .setThresholdUnder(thresholdUnder)
                .build();
    }

    public static RBLMessage.Trigger buildTrigger(
            int instructionId,
            Iterable<String> parameters){
        return RBLMessage.Trigger.newBuilder()
                .setInstructionId(instructionId)
                .addAllParameters(parameters)
                .build();

    }


    public static RBLMessage.Range buildRange(
            int count,
            String startDateTime,
            String endDateTime){
        return RBLMessage.Range.newBuilder()
                .setCount(count)
                .setStartDateTime(startDateTime)
                .setEndDateTime(endDateTime)
                .build();

    }

    public static RBLMessage buildLogicMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            String name,
            Iterable<RBLMessage.Actuator> initiator,
            Iterable<RBLMessage.Condition> condition,
            Iterable<RBLMessage.Actuator> receiver,
            Iterable<RBLMessage.Trigger> trigger

    ){
        RBLMessage.Logic.Builder logicMessage = RBLMessage.Logic.newBuilder();
        logicMessage.setName(name);
        logicMessage.addAllCondition(condition);
        logicMessage.addAllInitiator(initiator);
        logicMessage.addAllTrigger(trigger);
        logicMessage.addAllReceiver(receiver);
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.LOGIC)
                .setLogic(logicMessage)
                .build();
    }

    //----------------------------------------------------------------------------------------------
    //                                      DATA
    //----------------------------------------------------------------------------------------------


    public static RBLMessage.DataSet buildDataSetMessage(
            RBLMessage.CrudType crudType,
            RBLMessage.DataType dataType,
            RBLMessage.Actuator actuator,
            int fieldId,
            RBLMessage.Range range,
            RBLMessage.Data data
    ){
        RBLMessage.DataSet.Builder dataSet = RBLMessage.DataSet.newBuilder()
                .setCrudType(crudType)
                .setDataType(dataType);

        if(actuator != null){
            dataSet.setActuator(actuator);
        }

        if(fieldId > 0){
            dataSet.setFieldId(fieldId);
        }

        if(range != null){
            dataSet.setRange(range);
        }

        if(data != null){
            dataSet.setData(data);
        }

        return dataSet.build();
    }


    public static RBLMessage.Data.Builder buildDataMessage(
            Iterable<RBLMessage.Actuator> actuators,
            Iterable<String> stringData,
            Iterable<Integer> int32Data,
            Iterable<Float> floatData){
        RBLMessage.Data.Builder dataMessage = RBLMessage.Data.newBuilder();

        if(actuators != null){
            dataMessage.addAllActuators(actuators);
        }

        if(stringData != null){
            dataMessage.addAllStringData(stringData);
        }

        if(int32Data != null){
            dataMessage.addAllInt32Data(int32Data);
        }

        if(floatData != null){
            dataMessage.addAllFloatData(floatData);
        }
        return dataMessage;
    }

    //----------------------------------------------------------------------------------------------
    //                                      AUTH
    //----------------------------------------------------------------------------------------------

    public static RBLMessage buildAuthMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            String key){
        return createBaseMessage(id, messageFlag, RBLMessage.MessageType.AUTH)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                        .setText(key))
                .build();
    }



}
