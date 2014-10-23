package protobuf;

import java.util.List;
import protobuf.RBHproto.*;

/**
 * Created by Peter Mösenthin.
 *
 * The ProtoFactory was created for easy access to Protobuf messages
 * to even further reduce code to send messages.
 */
public class ProtoFactory {

    public static final RBHMessage.DataType DATA_TYPE_FLOAT =
            RBHMessage.DataType.FLOAT;
    public static final RBHproto.RBHMessage.DataType DATA_TYPE_INTEGER =
            RBHMessage.DataType.INTEGER;
    public static final RBHproto.RBHMessage.DataType DATA_TYPE_STRING =
            RBHMessage.DataType.STRING;


    private static RBHMessage.Builder createBaseMessage(
            String id,
            RBHMessage.MessageType messageType){
        return RBHMessage.newBuilder()
                .setMType(messageType)
                .setId(id);
    }

    public static RBHMessage buildPlainTextMessage(
            String id,
            String plainText){
        return createBaseMessage(id, RBHMessage.MessageType.PLAIN_TEXT)
                .setPlainText(
                        RBHMessage.PlainText.newBuilder()
                                .setText(plainText))
                .build();
    }

    public static RBHMessage.Instruction.Builder buildInstructionMessage(
            String instructionId,
            List<String> stringParameters,
            List<Integer> intParameters){
         RBHMessage.Instruction.Builder instruction =
                 RBHMessage.Instruction.newBuilder();
                instruction.setInstructionID(instructionId);
        if(stringParameters != null){
            instruction.addAllStringParameters(stringParameters);
        }
        if(intParameters != null){
            instruction.addAllIntParameters(intParameters);
        }
        return instruction;
    }

    public static RBHMessage buildRegisterInstructionObserverMessage(
            String id,
            String moduleId,
            RBHMessage.Instruction.Builder instruction){
        return createBaseMessage(
                id,
                RBHMessage.MessageType.REGISTER_INSTRUCTION_OBSERVER)
                .setRegisterInstructionObserver(
                        RBHMessage.RegisterInstructionObserver.newBuilder()
                        .setModuleID(moduleId)
                        .setInstruction(instruction)
                )
                .build();
    }

    public static RBHMessage buildRunInstructionMessage(
            String id,
            String targetId,
            RBHMessage.Instruction.Builder instruction){
        return createBaseMessage(
                id,
                RBHMessage.MessageType.RUN_INSTRUCTION)
                .setRunInstruction(RBHMessage.RunInstruction.newBuilder()
                        .setTargetID(targetId)
                        .setInstruction(instruction)

                )
                .build();
    }

    public static RBHMessage buildRegisterDataFieldMessage(
            String id,
            String fieldName,
            RBHMessage.DataType dataType){
       return createBaseMessage(
               id,
               RBHMessage.MessageType.REGISTER_DATA_FIELD)
               .setRegisterDataField(RBHMessage.RegisterDataField.newBuilder()
                       .setDType(dataType)
                       .setFieldName(fieldName)
               ).build();
    }

    public static RBHMessage.Data.Builder createDataMessge(
            RBHMessage.DataType dataType,
            String dateTime,
            Object data){
        RBHMessage.Data.Builder dataMessage = RBHMessage.Data.newBuilder();
        dataMessage.setDType(dataType)
                .setDateTime(dateTime);
        if(dataType == RBHMessage.DataType.FLOAT){
            if(data instanceof Float) {
                dataMessage.setFloatData((Float) data);
            }
        }
        else if(dataType == RBHMessage.DataType.INTEGER){
            if(data instanceof Integer){
                dataMessage.setInt32Data((Integer) data);
            }
        }
        else if(dataType == RBHMessage.DataType.STRING){
            if(data instanceof String){
                dataMessage.setStringData((String) data);
            }
        }

        return dataMessage;
    }

    public static RBHMessage buildUpdateDataFieldMessage(
            String id,
            String fieldName,
            RBHMessage.Data.Builder data){
        return createBaseMessage(
                id,
                RBHMessage.MessageType.UPDATE_DATA_FIELD)
                .setUpdateDataField(RBHMessage.UpdateDataField.newBuilder()
                        .setFieldName(fieldName)
                        .setData(data)
                ).build();
    }

    public static RBHMessage buildGetDataSetMessage(
            String id,
            String modulId,
            String fieldId,
            int count,
            String startDateTime,
            String endDateTime){
        RBHMessage.Builder message = createBaseMessage(id,
                RBHMessage.MessageType.GET_DATA_SET);
        RBHMessage.GetDataSet.Builder gds = RBHMessage.GetDataSet.newBuilder();
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

    public static RBHMessage buildDataSetMessage(
            String id,
            String modulId,
            String fieldId,
            List<? super Object> dataList,
            RBHMessage.DataType dataType){
        RBHMessage.Builder message =
                createBaseMessage(id, RBHproto.RBHMessage.MessageType.DATA_SET);
        RBHMessage.DataSet.Builder dataSet = RBHproto.RBHMessage
                .DataSet
                .newBuilder();
        dataSet.setModulID(modulId);
        dataSet.setFieldID(fieldId);
        RBHMessage.Data.Builder data = RBHproto.RBHMessage.Data
                .newBuilder();
        if(dataType == RBHMessage.DataType.FLOAT){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setFloatData((Float) o);
                dataSet.addData(data);
            }
        }
        else if(dataType == RBHMessage.DataType.INTEGER){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setInt32Data((Integer) o);
                dataSet.addData(data);
            }
        }
        else if(dataType == RBHMessage.DataType.STRING){
            data.setDType(dataType);
            for(Object o : dataList) {
                data.setStringData((String) o);
                dataSet.addData(data);
            }
        }
        message.setDataSet(dataSet);
        return message.build();
    }

    public static RBHMessage buildAuthRequestMessage(
            String id,
            String key){
        return createBaseMessage(id, RBHMessage.MessageType.AUTH_REQUEST)
                .setPlainText(
                        RBHproto.RBHMessage.PlainText.newBuilder()
                        .setText(key))
                .build();
    }

    public static RBHMessage buildAuthDeniedMessage(
            String id,
            String reason){
        return createBaseMessage(id, RBHMessage.MessageType.AUTH_DENIED)
                .setPlainText(
                        RBHMessage.PlainText.newBuilder()
                        .setText(reason))
                .build();
    }

    public static RBHMessage buildAuthAcceptMessage(
            String id,
            String message){
        return createBaseMessage(id, RBHMessage.MessageType.AUTH_ACCEPT)
                .setPlainText(
                        RBHMessage.PlainText.newBuilder()
                                .setText(message)
                )
                .build();
    }
}
