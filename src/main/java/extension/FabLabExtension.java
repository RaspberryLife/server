package extension;

import com.google.common.eventbus.Subscribe;
import event.SerialMessageEvent;
import system.service.EventBusService;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class FabLabExtension implements Extension{

    public static final String DEBUG_TAG = FabLabExtension.class.getSimpleName();

    public static final int MODULE_OUTLET = 1;
    public static final int MODULE_TEMP = 2;
    public static final int MODULE_PIR = 3;
    public static final int MODULE_REED = 4;
    public static final int MODULE_LUMINOSITY = 5;
    public static final int MODULE_RELAY = 6;
    public static final int MODULE_PIR_AND_RELAY = 7;


    @Override
    public void init() {
        EventBusService.register(this);
    }
    
    @Subscribe
    public void handleSerialMessageEvent(SerialMessageEvent message){
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
        if(message.instructionId == 1){ // Reed status update

        }
    }

    private void handlePirMessage(SerialMessageEvent message) {
        if(message.instructionId == 1){ // Peer registered movement

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
