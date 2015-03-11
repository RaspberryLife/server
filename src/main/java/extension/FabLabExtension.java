package extension;

import com.google.common.eventbus.Subscribe;
import event.SerialMessageEvent;
import system.service.EventBusService;
import util.Log;

import java.util.HashMap;

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

    private HashMap<Integer,Integer> moduleStates = new HashMap<>();


    @Override
    public void init() {
        EventBusService.register(this);

    }
    
    @Subscribe
    public void handleSerialMessageEvent(SerialMessageEvent message){
        switch(message.moduleType){
            case MODULE_REED:
                handleReedMessage(message);
                break;
        }
    }

    private void startDailyCheckJob(){

    }

    private void handleReedMessage(SerialMessageEvent message) {
        Log.add(DEBUG_TAG, "Received Reed " + message.rawContent);
        if(message.instructionId == 1){ // Reed status update / new status
            message.buildModel();
            int param0 = Integer.parseInt(message.parameters.get(0)); // 0 = open / 1 = closed
            moduleStates.put(message.moduleId, param0);
            updateDisplay();
        }
    }

    private void updateDisplay(){
        if(moduleStates.containsValue(0)){
            String displayText = "Fensterstatus: " + moduleStates.toString();
            //TODO send text to display
        }
    }










}
