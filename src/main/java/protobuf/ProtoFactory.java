package protobuf;

import java.util.List;

import protobuf.RblProto.*;

/**
 * Created by Peter Mösenthin.
 *
 * The ProtoFactory was created for easy access to Protobuf messages
 * to even further reduce code to send messages.
 */
public class ProtoFactory {

    //==========================================================================
    // BASE
    //==========================================================================

    private static RBLMessage.Builder createBaseMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.MessageType messageType){
        return RBLMessage.newBuilder()
                .setId(id)
                .setMessageType(messageType)
                .setMessageFlag(messageFlag);
    }

    //==========================================================================
    // PLAIN TEXT
    //==========================================================================

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

    //==========================================================================
    // INSTRUCTION
    //==========================================================================

    public static RBLMessage.Instruction.Builder buildInstructionMessage(
            int instructionId,
            List<String> stringParameters,
            List<Integer> intParameters){
        RBLMessage.Instruction.Builder instruction = RBLMessage.Instruction.newBuilder();
        instruction.setInstructionId(instructionId);
        if(stringParameters != null){
            instruction.addAllStringParameters(stringParameters);
        }
        if(intParameters != null){
            instruction.addAllIntParameters(intParameters);
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

    //==========================================================================
    // LOGIC
    //==========================================================================

    public static RBLMessage.Actuator.Builder buildActuator(
            RBLMessage.ActuatorType actuatorType,
            int actuatorId){
        return RBLMessage.Actuator.newBuilder()
                .setActuatorId(actuatorId)
                .setActuatorType(actuatorType);
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

    public static RBLMessage buildLogicMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            String name,
            RBLMessage.Actuator.Builder initiator,
            RBLMessage.Actuator.Builder receiver,
            RBLMessage.Condition.Builder condition
    ){
        RBLMessage.Logic.Builder logicMessage = RBLMessage.Logic.newBuilder();
        logicMessage.setName(name);
        logicMessage.setCondition(condition);
        logicMessage.setInitiator(initiator);
        logicMessage.setReceiver(receiver);
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.LOGIC)
                .setLogic(logicMessage)
                .build();
    }


    //==========================================================================
    // DATA
    //==========================================================================

    public static RBLMessage buildSetDataMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.Actuator actuator,
            int fieldId,
            RBLMessage.Data data){
        RBLMessage.SetData.Builder setDataMessage = RBLMessage.SetData.newBuilder();
        setDataMessage.setData(data);
        setDataMessage.setFieldId(fieldId);
        setDataMessage.setActuator(actuator);
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.SET_DATA)
                .setSetData(setDataMessage).build();
    }

    public static RBLMessage buildGetDataMessage(
            String id,
            RBLMessage.MessageFlag messageFlag,
            RBLMessage.Actuator actuator,
            int fieldId,
            RBLMessage.Range.Builder range
    ){
        RBLMessage.GetData.Builder getDataMessage = RBLMessage.GetData.newBuilder();
        getDataMessage.setFieldId(fieldId);
        getDataMessage.setActuator(actuator);
        if(range != null){
            getDataMessage.setRange(range);
        }
        return createBaseMessage(
                id, messageFlag,
                RBLMessage.MessageType.GET_DATA)
                .setGetData(getDataMessage).build();
    }

    public static RBLMessage.Range.Builder buildRange(
            int count,
            String startDateTime,
            String endDateTime){
        return RBLMessage.Range.newBuilder()
                .setCount(count)
                .setStartDateTime(startDateTime)
                .setEndDateTime(endDateTime);
    }


    public static RBLMessage.Data.Builder buildDataMessage(
            int fieldId,
            RBLMessage.DataType dataType,
            Iterable<String> stringData,
            Iterable<Integer> int32Data,
            Iterable<Float> floatData){
        RBLMessage.Data.Builder dataMessage = RBLMessage.Data.newBuilder();
        dataMessage.setDataType(dataType);
        dataMessage.setFieldId(fieldId);

        if(stringData != null && dataType == RBLMessage.DataType.STRING){
            dataMessage.addAllStringData(stringData);
        }

        if(int32Data != null && dataType == RBLMessage.DataType.INTEGER){
            dataMessage.addAllInt32Data(int32Data);
        }

        if(floatData != null && dataType == RBLMessage.DataType.FLOAT){
            dataMessage.addAllFloatData(floatData);
        }

        return dataMessage;
    }

    //==========================================================================
    // AUTH
    //==========================================================================

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
