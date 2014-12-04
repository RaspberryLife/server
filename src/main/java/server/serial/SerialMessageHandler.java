package server.serial;

import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import event.message.ModuleInstruction;
import event.message.SerialMessage;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import event.EventBusService;
import system.Config;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

    public static final int MODULE_OUTLET = 1;
    public static final int MODULE_TEMP = 2;
    public static final int MODULE_PIR = 3;
    public static final int MODULE_REED = 4;
    public static final int MODULE_LUMINOSITY = 5;
    public static final int MODULE_RELAY = 6;
    public static final int MODULE_PIR_AND_RELAY = 7;

    //----------------------------------------------------------------------------------------------
    //                                      POPULATION & SYSTEM CHECK
    //----------------------------------------------------------------------------------------------

    @Subscribe
    public void handleSerialMessage(SerialMessage message) {
        Log.add(DEBUG_TAG, "Received message: " + message.rawContent);
        message.populateBase();
        preSwitchOnInstruction(message);
    }


    private void preSwitchOnInstruction(SerialMessage message){
        switch (message.instructionId){
            case 99:
                broadcastDebug(message);
                break;
            case 98:
                readHeartbeat(message);
            default:
                switchModelType(message);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      SYSTEM FUNCTIONALITY
    //----------------------------------------------------------------------------------------------

    private void readHeartbeat(SerialMessage message) {
        String voltage = "";
        String preColon = message.parameters.get(2).substring(0,1);
        String postColon = message.parameters.get(2).substring(2,3);
        voltage = preColon + "," + postColon + "V";

        Log.add(DEBUG_TAG, "Received heartbeat "
                + "Type: " + message.moduleType
                + "Id: " + message.moduleId
                + "State: " + message.parameters.get(0)
                + "Frequency: " + message.parameters.get(1)
                + "Battery voltage : " + voltage
        );
    }

    //----------------------------------------------------------------------------------------------
    //                                      NORMAL MESSAGES
    //----------------------------------------------------------------------------------------------


    private void switchModelType(SerialMessage message) {
        switch(message.moduleType){
            case MODULE_OUTLET:
                handleOutletMessage(message);
                break;
            case MODULE_TEMP:
                handleTempMessage(message);
                break;
            case MODULE_PIR:
                handlePirMessage(message);
                break;
            case MODULE_REED:
                handleReedMessage(message);
                break;
            case MODULE_LUMINOSITY:
                handleLuminosityMessage(message);
                break;
            case MODULE_RELAY:
                break;
            case MODULE_PIR_AND_RELAY:
                break;
        }

    }

    private void handleLuminosityMessage(SerialMessage message) {
        //TODO implement
    }

    private void handleReedMessage(SerialMessage message) {
        //TODO implement
    }

    private void handlePirMessage(SerialMessage message) {
        //TODO implement
    }

    private void handleTempMessage(SerialMessage message) {
        //TODO implement
    }

    private void handleOutletMessage(SerialMessage message) {
        //TODO implement
    }

    private void broadcastDebug(SerialMessage message) {
        Log.add(DEBUG_TAG, "Received serial debug " + message.rawContent);
        ClientHandler.broadcastMessage(
                ProtoFactory.buildPlainTextMessage(
                        Config.getConf().getString("server.id"),
                        RblProto.RBLMessage.MessageFlag.RESPONSE,
                        "Serial connector received message: " +
                                message.rawContent
                ));
        ModuleInstruction mi = new ModuleInstruction();

        mi.setType(message.moduleType);
        mi.setModuleId(message.moduleId);
        mi.setInstructionId(message.instructionId);
        mi.setParameters(message.parameters);
        EventBusService.post(mi);
    }



}
