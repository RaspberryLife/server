package server.serial;

import com.google.common.eventbus.Subscribe;
import data.model.Module;
import event.ModuleEvent;
import event.NotificationEvent;
import event.SerialMessageEvent;
import protobuf.RblProto;
import system.service.DataBaseService;
import system.service.EventBusService;
import util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
public class SerialMessageHandler {

    public static final String DEBUG_TAG = SerialMessageHandler.class.getSimpleName();

    // Module types
    public static final int MODULE_OUTLET = 1;
    public static final int MODULE_TEMP = 2;
    public static final int MODULE_PIR = 3;
    public static final int MODULE_REED = 4;
    public static final int MODULE_LUMINOSITY = 5;
    public static final int MODULE_RELAY = 6;
    public static final int MODULE_PIR_AND_RELAY = 7;

    // Instruction ID - used to detect system types early on
    public static final int IID_DEBUG_BROADCAST = 99;
    public static final int IID_HEARTBEAT = 98;
    public static final int IID_MANAGE_ADDRESS = 95;

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
            case IID_DEBUG_BROADCAST:
                broadcastDebug(message);
                break;
            case IID_HEARTBEAT :
                readHeartbeat(message);
                break;
            case IID_MANAGE_ADDRESS:
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
        if(message.parameters.isEmpty()){ // Module is requesting
            Log.add(DEBUG_TAG, "Module requested address");
            if(message.moduleType == MODULE_OUTLET){
                Log.add(DEBUG_TAG, "Requesting module is of type OUTLET. Not generating address");
            } else {
                String address = DataBaseService.getInstance().generateSerialAddress();
                sendAddressMessage(message, address);
                Module m = new Module();
                m.setSerial_address(address);
                m.setType(matchDatabaseType(message.moduleType));
                DataBaseService.getInstance().insert(m);
            }
        } else { // Module has address
            String address = message.parameters.get(0);
            Log.add(DEBUG_TAG, "Module sent address: " + address);
            if(DataBaseService.getInstance().moduleExists(address)){
                sendAddressMessage(message, address);
                //TODO update database entry
            }
        }
    }

    private String matchDatabaseType(int type){
        switch(type){
            case MODULE_OUTLET:
                return Module.TYPE_OUTLET;
            case MODULE_TEMP:
                return Module.TYPE_TEMP;
            case MODULE_PIR:
                return Module.TYPE_PIR;
            case MODULE_REED:
                return Module.TYPE_REED;
            case MODULE_LUMINOSITY:
                return Module.TYPE_LUMINOSITY;
            case MODULE_RELAY:
                return Module.TYPE_RELAY;
            case MODULE_PIR_AND_RELAY:
                return Module.TYPE_PIR_AND_RELAY;
        }
        return null;
    }

    private void sendAddressMessage(SerialMessageEvent message, String address){
        ModuleEvent me = new ModuleEvent();
        me.setType(message.moduleType);
        me.setModuleId(message.moduleId);
        me.setInstructionId(IID_MANAGE_ADDRESS);
        List modules = DataBaseService.getInstance().readAll(DataBaseService.DataType.MODULE);
        me.getParameters().add(address);
        EventBusService.post(me);
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
            ModuleEvent me = new ModuleEvent();
            me.setType(MODULE_OUTLET);
            me.setModuleId(1);
            me.setInstructionId(2);
            List<String> params = new ArrayList<String>();
            params.add("01");
            params.add("01");
            params.add("04");
            me.setParameters(params);
            EventBusService.post(me);
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

}
