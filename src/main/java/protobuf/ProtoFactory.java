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

    public static final RBLMessage.DataType DATA_TYPE_FLOAT =
            RBLMessage.DataType.FLOAT;
    public static final RBLMessage.DataType DATA_TYPE_INTEGER =
            RBLMessage.DataType.INTEGER;
    public static final RBLMessage.DataType DATA_TYPE_STRING =
            RBLMessage.DataType.STRING;


    private static RBLMessage.Builder createBaseMessage(
            String id,
            RBLMessage.MessageType messageType){
        return RBLMessage.newBuilder()
                .setMType(messageType)
                .setId(id);
    }

    public static RBLMessage buildPlainTextMessage(
            String id,
            String plainText){
        return createBaseMessage(id, RBLMessage.MessageType.PLAIN_TEXT)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                                .setText(plainText))
                .build();
    }

    public static RBLMessage.Instruction.Builder buildInstructionMessage(
            int instructionId,
            List<String> stringParameters,
            List<Integer> intParameters){
         RBLMessage.Instruction.Builder instruction =
                 RBLMessage.Instruction.newBuilder();
                instruction.setInstructionID(instructionId);
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
            RBLMessage.ModelType modelType,
            int targetModuleId,
            RBLMessage.Instruction.Builder instruction){
        return createBaseMessage(
                id,
                RBLMessage.MessageType.RUN_INSTRUCTION)
                .setRunInstruction(RBLMessage.RunInstruction.newBuilder()
                        .setTargetModulID(targetModuleId)
                        .setModeltype(modelType)
                        .setInstruction(instruction)

                ).build();
    }

    public static RBLMessage buildRegisterDataFieldMessage(
            String id,
            String fieldName,
            RBLMessage.DataType dataType){
       return createBaseMessage(
               id,
               RBLMessage.MessageType.REGISTER_DATA_FIELD)
               .setRegisterDataField(RBLMessage.RegisterDataField.newBuilder()
                       .setDType(dataType)
                       .setFieldName(fieldName)
               ).build();
    }

    public static RBLMessage.Data.Builder createDataMessge(
            RBLMessage.DataType dataType,
            String dateTime,
            Object data){
        RBLMessage.Data.Builder dataMessage = RBLMessage.Data.newBuilder();
        dataMessage.setDType(dataType)
                .setDateTime(dateTime);
        if(dataType == RBLMessage.DataType.FLOAT){
            if(data instanceof Float) {
                dataMessage.setFloatData((Float) data);
            }
        }
        else if(dataType == RBLMessage.DataType.INTEGER){
            if(data instanceof Integer){
                dataMessage.setInt32Data((Integer) data);
            }
        }
        else if(dataType == RBLMessage.DataType.STRING){
            if(data instanceof String){
                dataMessage.setStringData((String) data);
            }
        }

        return dataMessage;
    }

    public static RBLMessage buildUpdateDataFieldMessage(
            String id,
            String fieldName,
            RBLMessage.Data.Builder data){
        return createBaseMessage(
                id,
                RBLMessage.MessageType.UPDATE_DATA_FIELD)
                .setUpdateDataField(RBLMessage.UpdateDataField.newBuilder()
                        .setFieldName(fieldName)
                        .setData(data)
                ).build();
    }

    public static RBLMessage buildGetDataSetMessage(
            String id,
            String modulId,
            String fieldId,
            int count,
            String startDateTime,
            String endDateTime){
        RBLMessage.Builder message = createBaseMessage(id,
                RBLMessage.MessageType.GET_DATA_SET);
        RBLMessage.GetDataSet.Builder gds = RBLMessage.GetDataSet.newBuilder();
        gds.setModulID(modulId);
        gds.setFieldID(fieldId);
        if(count > 0){
            gds.setCount(count);
        }
        if(!startDateTime.isEmpty()){
            gds.setStartDateTime(startDateTime);
        }
        if(!endDateTime.isEmpty()){
            gds.setEndDateTime(endDateTime);
        }
        message.setGetDataSet(gds);
        return message.build();
    }

    public static RBLMessage buildDataSetMessage(
            String id,
            String modulId,
            String fieldId,
            List<? super Object> dataList,
            RBLMessage.DataType dataType){
        RBLMessage.Builder message =
                createBaseMessage(id, RBLMessage.MessageType.DATA_SET);
        RBLMessage.DataSet.Builder dataSet = RBLMessage
                .DataSet
                .newBuilder();
        dataSet.setModulID(modulId);
        dataSet.setFieldID(fieldId);
        RBLMessage.Data.Builder data = RBLMessage.Data
                .newBuilder();
        if(dataType == RBLMessage.DataType.FLOAT){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setFloatData((Float) o);
                dataSet.addData(data);
            }
        }
        else if(dataType == RBLMessage.DataType.INTEGER){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setInt32Data((Integer) o);
                dataSet.addData(data);
            }
        }
        else if(dataType == RBLMessage.DataType.STRING){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setStringData((String) o);
                dataSet.addData(data);
            }
        }
        message.setDataSet(dataSet);
        return message.build();
    }

    public static RBLMessage buildAuthRequestMessage(
            String id,
            String key){
        return createBaseMessage(id, RBLMessage.MessageType.AUTH_REQUEST)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                        .setText(key))
                .build();
    }

    public static RBLMessage buildAuthDeniedMessage(
            String id,
            String reason){
        return createBaseMessage(id, RBLMessage.MessageType.AUTH_DENIED)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                        .setText(reason))
                .build();
    }

    public static RBLMessage buildAuthAcceptMessage(
            String id,
            String message){
        return createBaseMessage(id, RBLMessage.MessageType.AUTH_ACCEPT)
                .setPlainText(
                        RBLMessage.PlainText.newBuilder()
                                .setText(message)
                )
                .build();
    }
}
