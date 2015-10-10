package rbl.extension.fablab;

import com.google.common.eventbus.Subscribe;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rbl.event.NotificationEvent;
import rbl.event.ScheduleEvent;
import rbl.event.SerialMessageEvent;
import rbl.extension.Extension;
import rbl.extension.fablab.response.FabLabStatus;
import rbl.extension.fablab.response.WindowModule;
import rbl.scheduling.RepeatInterval;
import rbl.serial.SerialTypeResolver;
import rbl.event.EventBusService;
import rbl.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 */
@RestController
public class FabLabExtension implements Extension, Job
{

	public static final String DEBUG_TAG = FabLabExtension.class.getSimpleName();
	private static List<WindowModule> windowModules = new ArrayList<>();

	// 24h
	public static final int DAILY_CHECK_HOUR = 19;
	public static final int DAILY_CHECK_MINUTE = 49;

	@Override
	public void init()
	{
		EventBusService.register(this);
		Log.add(DEBUG_TAG, "Initializing FabLabExtension: Notifying user about open windows daily at "
				+ DAILY_CHECK_HOUR + ":" + DAILY_CHECK_MINUTE);
		startDailyCheckJob();
		//test();
	}

	@RequestMapping("/rbl/extension/fablab/modules")
	public FabLabStatus status() {
		return new FabLabStatus(windowModules);
	}

	@Subscribe
	public void handleSerialMessageEvent(SerialMessageEvent message)
	{
		if (message.getMessageType() == SerialMessageEvent.Type.SEND)
		{
			return;
		}
		Log.add(DEBUG_TAG, "Received message " + message.toString());
		message.buildModel();
		switch (message.getModuleType())
		{
			case SerialTypeResolver.MODULE_REED:
				handleReedMessage(message);
				break;
		}
	}

	private void startDailyCheckJob()
	{
		ScheduleEvent e = new ScheduleEvent(ScheduleEvent.Type.EXTENSION);
		e.setJob(this);
		e.setIdentity("FabLabExtensionJob");
		e.getInterval().put(RepeatInterval.HOUR, DAILY_CHECK_HOUR);
		e.getInterval().put(RepeatInterval.MINUTE, DAILY_CHECK_MINUTE);
		EventBusService.post(e);
	}

	private void handleReedMessage(SerialMessageEvent message)
	{
		Log.add(DEBUG_TAG, "Received Reed " + message.getRawContent());
		if (message.getInstructionId() == 1)
		{ // Reed status update / new status
			int param0 = Integer.parseInt(message.getParameters().get(0)); // 0 = open / 1 = closed
			windowModules.add(new WindowModule(message.getModuleId(), param0));
			updateDisplay(isAnyWindowOpen());
		}
	}

	@RequestMapping("/rbl/extension/fablab/anyWindowOpen")
	private boolean isAnyWindowOpen(){
		for(WindowModule wm : windowModules){
			if(wm.getState() == 0) // 0 = open / 1 = closed
			{
				return true;
			}
		}
		return false;
	}

	private void updateDisplay(boolean windowOpen)
	{
		SerialMessageEvent me = new SerialMessageEvent();
		me.setMessageType(SerialMessageEvent.Type.SEND);
		me.setModuleType(SerialTypeResolver.MODULE_STATUS_MONITOR);
		me.setModuleId(1);
		me.setInstructionId(1);
		if (windowOpen)
		{
			//min 1 Fenster auf
			me.getParameters().add("2");
		}
		else
		{
			//kein Fenster auf
			me.getParameters().add("1");
		}
		EventBusService.post(me);
	}

	private String getDisplayText()
	{
		return "Fensterstatus: " + windowModules.toString();
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		NotificationEvent ne = new NotificationEvent(NotificationEvent.Type.CLIENT_EMAIL,
				"Fenster ist noch offen!\n" + getDisplayText());
		EventBusService.post(ne);
	}

	private void test()
	{
		Log.add(DEBUG_TAG, "Testing");
		SerialMessageEvent sme;
		for (int i = 0; i < 10; i++)
		{
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
