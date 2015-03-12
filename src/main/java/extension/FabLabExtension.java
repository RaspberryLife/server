package extension;

import com.google.common.eventbus.Subscribe;
import event.ModuleEvent;
import event.ScheduleEvent;
import event.SerialMessageEvent;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import scheduling.RepeatInterval;
import system.service.EventBusService;
import util.Log;

import java.util.HashMap;

/**
 * Created by Peter Mösenthin.
 */
public class FabLabExtension implements Extension, Job {

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
        startDailyCheckJob();
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
        ScheduleEvent e = new ScheduleEvent(ScheduleEvent.Type.EXTENSION);
        e.setJob(this);
        e.setIdentity("FabLabExtensionJob");
        e.getInterval().put(RepeatInterval.HOUR, 17);
        e.getInterval().put(RepeatInterval.MINUTE,0);
        EventBusService.post(e);
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
            ModuleEvent me = new ModuleEvent();
            //me.setType(message.moduleType);
            //me.setModuleId(message.moduleId);
            //me.setInstructionId(message.instructionId);
            me.getParameters().add(displayText);
            EventBusService.post(me);
            //TODO send text to display
        }
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }


}
