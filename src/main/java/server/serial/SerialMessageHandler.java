package server.serial;

import com.google.common.eventbus.Subscribe;
import data.model.Module;
import event.ModuleEvent;
import event.NotificationEvent;
import event.SerialMessageEvent;
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
        message.buildModel();
        preSwitchOnInstruction(message);
    }

    /**
     * Pass message to IID method 
     * @param message
     */
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
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      SYSTEM FUNCTIONALITY
    //----------------------------------------------------------------------------------------------

    /**
     * Handle module heartbeat message
     * @param message
     */
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

    /**
     * Handle address request by modules
     * @param message
     */
    private void manageAddress(SerialMessageEvent message) {
        if(message.parameters.isEmpty()){ // Module is requesting
            Log.add(DEBUG_TAG, "Module requested address");
            if(message.moduleType == SerialTypeResolver.MODULE_OUTLET){
                Log.add(DEBUG_TAG, "Requesting module is of type OUTLET. Not generating address");
            } else {
                String address = SerialAddress.generate();
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
    
    /**
     * Matches the serial module type to the database model
     * @param message
     */
    public static String matchDatabaseType(int type){
        switch(type){
            case SerialTypeResolver.MODULE_OUTLET:
                return Module.TYPE_OUTLET;
            case SerialTypeResolver.MODULE_TEMP:
                return Module.TYPE_TEMP;
            case SerialTypeResolver.MODULE_PIR:
                return Module.TYPE_PIR;
            case SerialTypeResolver.MODULE_REED:
                return Module.TYPE_REED;
            case SerialTypeResolver.MODULE_LUMINOSITY:
                return Module.TYPE_LUMINOSITY;
            case SerialTypeResolver.MODULE_RELAY:
                return Module.TYPE_RELAY;
            case SerialTypeResolver.MODULE_PIR_AND_RELAY:
                return Module.TYPE_PIR_AND_RELAY;
        }
        return null;
    }


    /**
     *  Send a message containing the generated address to the requesting module
     * @param message
     * @param address
     */
    private void sendAddressMessage(SerialMessageEvent message, String address){
        ModuleEvent me = new ModuleEvent();
        me.setType(message.moduleType);
        me.setModuleId(message.moduleId);
        me.setInstructionId(IID_MANAGE_ADDRESS);
        //List modules = DataBaseService.getInstance().readAll(DataBaseService.DataType.MODULE);
        me.getParameters().add(address);
        EventBusService.post(me);
    }

    /**
     * Notify every client with a message from a module
     * @param message
     */
    private void broadcastDebug(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received debug " + message.rawContent);

        // Client broadcast
        String bc_message = "Serial connector received message: " + message.rawContent;
        NotificationEvent e = new NotificationEvent(
                NotificationEvent.Type.CLIENT_BROADCAST,bc_message);
        EventBusService.post(e);

        // Module broadcast
        ModuleEvent me = new ModuleEvent();
        me.setType(message.moduleType);
        me.setModuleId(message.moduleId);
        me.setInstructionId(message.instructionId);
        me.setParameters(message.parameters);
        EventBusService.post(me);
    }
    
}
