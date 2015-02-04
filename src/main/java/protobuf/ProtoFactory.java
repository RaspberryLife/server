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
            List<RBLMessage.PlainText> plainTexts){
        return createBaseMessage(id, flag, RBLMessage.MessageType.PLAIN_TEXT)
                .addAllPlainText(plainTexts)
                .build();
    }

    public static RBLMessage buildPlainTextMessage(
            String id,
            RBLMessage.MessageFlag flag,
            RBLMessage.PlainText.Builder plainText){
        return createBaseMessage(id, flag, RBLMessage.MessageType.PLAIN_TEXT)
                .addPlainText(plainText)
                .build();
    }

    public static RBLMessage.PlainText.Builder buildPlainText(String plainText){
        return RBLMessage.PlainText.newBuilder()
                .setText(plainText);
    }

    //----------------------------------------------------------------------------------------------
    //                                      USER
    //----------------------------------------------------------------------------------------------

    public static RBLMessage.User.Builder buildUser(int id, String email, String password){
        RBLMessage.User.Builder ub = RBLMessage.User.newBuilder().setId(id);
        if(email != null){
            ub.setEmail(email);
        }
        if(password != null){
            ub.setPassword(password);
        }
        return ub;
    }

    //----------------------------------------------------------------------------------------------
    //                                      INSTRUCTION
    //----------------------------------------------------------------------------------------------

    public static RBLMessage.Instruction.Builder buildInstruction(
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
            List<RBLMessage.RunInstruction> runInstructions){
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.RUN_INSTRUCTION)
                .addAllRunInstruction(runInstructions)
                .build();
    }

    public static RBLMessage.RunInstruction buildRunInstruction(
            RBLMessage.Instruction instruction,
            RBLMessage.Actuator actuator){
        return RBLMessage.RunInstruction.newBuilder()
                .setInstruction(instruction)
                .setActuator(actuator)
                .build();
    }

    //----------------------------------------------------------------------------------------------
    //                                      LOGIC
    //----------------------------------------------------------------------------------------------

    public static RBLMessage.Logic.Builder buildLogic(
            RBLMessage.CrudType crudType,
            int logic_id,
            String name,
            Iterable<RBLMessage.LogicInitiator> initiator,
            Iterable<RBLMessage.LogicReceiver> receiver,
            RBLMessage.ExecutionFrequency executionFrequency,
            RBLMessage.ExecutionRequirement executionRequirement
    ){
        return RBLMessage.Logic.newBuilder().setName(name)
                .setCrudType(crudType)
                .setId(logic_id)
                .addAllLogicReceiver(receiver)
                .addAllLogicInitiator(initiator)
                .setExeFrequency(executionFrequency)
                .setExeRequirement(executionRequirement);
    }

    public static RBLMessage buildLogicMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
    List<RBLMessage.Logic> logic){
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.LOGIC)
                .addAllLogic(logic)
                .build();
    }

    public static RBLMessage.Actuator.Builder buildActuator(
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

        return actuator;
    }

    public static RBLMessage.LogicInitiator.Builder buildLogicInitiator(
            RBLMessage.Actuator.Builder actuator,
            RBLMessage.Condition.Builder condition){
        return RBLMessage.LogicInitiator.newBuilder()
                .setCondition(condition)
                .setInitiator(actuator);
    }

    public static RBLMessage.LogicReceiver.Builder buildLogicReceiver(
            RBLMessage.Actuator.Builder actuator,
            RBLMessage.Instruction.Builder instruction){
        return RBLMessage.LogicReceiver.newBuilder()
                .setInstruction(instruction)
                .setReceiver(actuator);
    }

    public static RBLMessage.Condition.Builder buildCondition(
            int fieldId,
            int thresholdOver,
            int thresholdUnder,
            boolean state){
        return RBLMessage.Condition.newBuilder()
                .setFieldId(fieldId)
                .setState(state)
                .setThresholdOver(thresholdOver)
                .setThresholdUnder(thresholdUnder);
    }

    public static RBLMessage.ExecutionFrequency.Builder buildExecutionFrequency(
            RBLMessage.ExecutionType executionType,
            int minute,
            int hour,
            int day,
            int week
    ){
        return RBLMessage.ExecutionFrequency.newBuilder()
                .setExeType(executionType)
                .setMinute(minute)
                .setHour(hour)
                .setDay(day)
                .setWeek(week);
    }


    //----------------------------------------------------------------------------------------------
    //                                      DATA
    //----------------------------------------------------------------------------------------------

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

    public static RBLMessage.DataSet buildDataSetMessage(
            RBLMessage.CrudType crudType,
            RBLMessage.DataType dataType,
            RBLMessage.Actuator actuator,
            int fieldId,
            RBLMessage.Range range,
            List<RBLMessage.Data> data
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
            dataSet.addAllData(data);
        }

        return dataSet.build();
    }


    public static RBLMessage.Data.Builder buildDataMessage(
            RBLMessage.Actuator actuator,
            String stringData,
            Integer int32Data,
            Float floatData){
        RBLMessage.Data.Builder dataMessage = RBLMessage.Data.newBuilder();

        RBLMessage.Data.newBuilder()
                .setActuator(actuator)
                .setStringData(stringData)
                .setInt32Data(int32Data)
                .setFloatData(floatData);
        return dataMessage;
    }

    //----------------------------------------------------------------------------------------------
    //                                      AUTH
    //----------------------------------------------------------------------------------------------

    public static RBLMessage buildAuthMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            List<RBLMessage.PlainText> key){
        return createBaseMessage(id, messageFlag, RBLMessage.MessageType.AUTH)
                .addAllPlainText(key)
                .build();
    }

    public static RBLMessage buildAuthMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.PlainText.Builder key){
        return createBaseMessage(id, messageFlag, RBLMessage.MessageType.AUTH)
                .addPlainText(key)
                .build();
    }



}
