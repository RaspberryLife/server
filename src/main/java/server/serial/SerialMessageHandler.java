package server.serial;

import com.google.common.eventbus.Subscribe;
import event.ModuleEvent;
import event.NotificationEvent;
import event.SerialMessageEvent;
import system.service.EventBusService;
import util.Log;

import java.util.ArrayList;
import java.util.List;

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
    public void handleSerialMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received message " + message.rawContent);
        message.populateBase();
        preSwitchOnInstruction(message);
    }


    private void preSwitchOnInstruction(SerialMessageEvent message){
        switch (message.instructionId){
            case 99:
                broadcastDebug(message);
                break;
            case 98:
                readHeartbeat(message);
                break;
            case 97:
                manageAddress(message);
                break;
            default:
                switchModelType(message);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      SYSTEM FUNCTIONALITY
    //----------------------------------------------------------------------------------------------

    private void readHeartbeat(SerialMessageEvent message) {
        String voltage = "";
        String preColon = message.parameters.get(2).substring(0, 1);
        String postColon = message.parameters.get(2).substring(2, 3);
        voltage = preColon + "," + postColon + "V";

        Log.add(DEBUG_TAG, "Received heartbeat "
                + "Type: " + message.moduleType
                + "Id: " + message.moduleId
                + "State: " + message.parameters.get(0)
                + "Frequency: " + message.parameters.get(1)
                + "Battery voltage : " + voltage
        );
    }

    private void manageAddress(SerialMessageEvent message) {
        if(message.parameters.isEmpty()){
            //TODO generate new address
            Log.add(DEBUG_TAG, "Module requested address");
            //TODO check available modules in database
            //TODO dont give addresses to outlet modules
            ModuleEvent me = new ModuleEvent();
            me.setType(message.moduleType);
            me.setModuleId(message.moduleId);
            me.setInstructionId(97);
            List<String> params = new ArrayList<String>();
            params.add("01");
            me.setParameters(params);
            EventBusService.post(me);
        } else {
            String address = message.parameters.get(0);
            Log.add(DEBUG_TAG, "Module sent address: " + address);
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      NORMAL MESSAGES
    //----------------------------------------------------------------------------------------------

    private void switchModelType(SerialMessageEvent message) {
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
                handleRelayMessage(message);
                break;
            case MODULE_PIR_AND_RELAY:
                handlePirAndRelayMessage(message);
                break;
        }
    }

    private void handlePirAndRelayMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received Pir/Relay " + message.rawContent);
        //TODO implement
    }

    private void handleRelayMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received Relay" + message.rawContent);
        //TODO implement
    }

    private void handleLuminosityMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received luminosity " + message.rawContent);
        //TODO implement
    }

    private void handleReedMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received Reed " + message.rawContent);
        //TODO implement
    }

    private void handlePirMessage(SerialMessageEvent message) {
        if(message.instructionId == 1){
            ModuleEvent me = new ModuleEvent();
            me.setType(MODULE_OUTLET);
            me.setModuleId(1);
            me.setInstructionId(1);
            List<String> params = new ArrayList<String>();
            params.add("01");
            params.add("01");
            params.add("04");
            me.setParameters(params);
            EventBusService.post(me);
        }
    }

    private void handleTempMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received Temp " + message.rawContent);
        //TODO implement
    }

    private void handleOutletMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received outlet " + message.rawContent);
        //TODO implement
    }

    private void broadcastDebug(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received debug " + message.rawContent);

        // Client broadcast
        String bc_message = "Serial connector received message: " + message.rawContent;
        NotificationEvent e = new NotificationEvent(
                NotificationEvent.Type.CLIENT_BROADCAST,bc_message);
        EventBusService.post(e);

        // Module broadcast
        ModuleEvent mi = new ModuleEvent();
        mi.setType(message.moduleType);
        mi.setModuleId(message.moduleId);
        mi.setInstructionId(message.instructionId);
        mi.setParameters(message.parameters);
        EventBusService.post(mi);
    }



}
