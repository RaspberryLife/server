package extension;

import com.google.common.eventbus.Subscribe;
import event.NotificationEvent;
import event.ScheduleEvent;
import event.SerialMessageEvent;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import scheduling.RepeatInterval;
import server.serial.SerialTypeResolver;
import system.service.EventBusService;
import util.Log;

import java.util.HashMap;

/**
 * Created by Peter Mösenthin.
 */
public class FabLabExtension implements Extension, Job {

	public static final String DEBUG_TAG = FabLabExtension.class.getSimpleName();

	private static HashMap<Integer,Integer> moduleStates = new HashMap<>();

	@Override
	public void init() {
		EventBusService.register(this);
		startDailyCheckJob();
		test();
	}

	@Subscribe
	public void handleSerialMessageEvent(SerialMessageEvent message) {
		if(message.getMessageType() == SerialMessageEvent.Type.SEND){
			return;
		}
		Log.add(DEBUG_TAG, "Received message " + message.toString());
		message.buildModel();
		switch (message.getModuleType()) {
			case SerialTypeResolver.MODULE_REED:
				handleReedMessage(message);
				break;
		}
	}

	private void startDailyCheckJob(){
		ScheduleEvent e = new ScheduleEvent(ScheduleEvent.Type.EXTENSION);
		e.setJob(this);
		e.setIdentity("FabLabExtensionJob");
		e.getInterval().put(RepeatInterval.HOUR, 19);
		e.getInterval().put(RepeatInterval.MINUTE, 49);
		EventBusService.post(e);
	}

	private void handleReedMessage(SerialMessageEvent message) {
		Log.add(DEBUG_TAG, "Received Reed " + message.getRawContent());
		if(message.getInstructionId() == 1){ // Reed status update / new status
			int param0 = Integer.parseInt(message.getParameters().get(0)); // 0 = open / 1 = closed
			moduleStates.put(message.getModuleId(), param0);
			updateDisplay(moduleStates.containsValue(0));
		}
	}

	private void updateDisplay(boolean open){
		if(moduleStates.containsValue(0)){
			SerialMessageEvent me = new SerialMessageEvent();
			me.setMessageType(SerialMessageEvent.Type.SEND);
			me.setModuleType(SerialTypeResolver.MODULE_STATUS_MONITOR);
			me.setModuleId(1);
			me.setInstructionId(1);
			if(open){
				me.getParameters().add("2");
			} else {
				me.getParameters().add("1");
			}
			EventBusService.post(me);
		}
	}

	private String getDisplayText(){
		return "Fensterstatus: " + moduleStates.toString();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		NotificationEvent ne = new NotificationEvent(NotificationEvent.Type.CLIENT_EMAIL, "Fenster ist noch offen!\n" + getDisplayText());
		EventBusService.post(ne);
	}

	private void test(){
		Log.add(DEBUG_TAG,"Testing");
		SerialMessageEvent sme;
		for (int i = 0; i< 10; i++){
			sme = new SerialMessageEvent();
			sme.setMessageType(SerialMessageEvent.Type.RECEIVE);
			sme.setModuleType(SerialTypeResolver.MODULE_REED);
			sme.setInstructionId(1);
			sme.setModuleId(i);
			sme.getParameters().add("" + i % 2);
			sme.buildSerial();
			EventBusService.post(sme);
		}
	}

}
