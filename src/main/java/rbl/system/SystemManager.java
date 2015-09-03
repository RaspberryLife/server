package rbl.system;

import com.google.common.eventbus.Subscribe;
import rbl.data.Config;
import rbl.event.ScheduleEvent;
import rbl.event.SystemEvent;
import rbl.extension.fablab.FabLabExtension;
import rbl.scheduling.RepeatInterval;
import rbl.serial.SerialConnector;
import rbl.system.service.DataBaseService;
import rbl.system.service.EventBusService;
import rbl.system.service.NotificationService;
import rbl.system.service.ScheduleService;
import rbl.util.Log;
import rbl.util.NetworkUtil;

/**
 * Created by Peter Mösenthin.
 */
public class SystemManager
{

	public static final String DEBUG_TAG = SystemManager.class.getSimpleName();

	private static SystemManager instance = new SystemManager();

	public static void register()
	{
		EventBusService.register(instance);
	}

	/**
	 * Private constructor for event access only
	 */
	private SystemManager()
	{
	}

	@Subscribe
	public void handleEvent(SystemEvent e)
	{
		switch (e.getType())
		{
			case START_SYSTEM:
				start();
				break;
			case STOP_SYSTEM:
				stop();
				break;
			case RESTART_SYSTEM:
				restart();
				break;
		}
	}

	/**
	 * Starts the serial
	 */
	private void start()
	{
		// 1. Load config
		loadConfig();
		// 2. Init event bus
		initEventBus();
		// 3. Do the rest
		initNotificationService();
		NetworkUtil.listIPAddresses();
		initSerialConnection();
		initDatabase();
		initScheduler();
		initExtensions();
	}

	private void stop()
	{
		Log.add(DEBUG_TAG, "Stopping ... (But not really)");
	}

	private void restart()
	{
		stop();
		start();
	}

	//----------------------------------------------------------------------------------------------
	//                                      STARTUP
	//----------------------------------------------------------------------------------------------

	/**
	 * Load the serial configurtion
	 */
	private void loadConfig()
	{
		Log.add(DEBUG_TAG, "Loading configuration");
		Config.readConfig();
		//Config.dumpConfig();
	}

	/**
	 * Start the eventbus service
	 */
	private void initEventBus()
	{
		EventBusService.init();
	}

	/**
	 * Register the serial connector in the eventbus for module communication
	 */
	private void initSerialConnection()
	{
		SerialConnector.register();
		EventBusService.post(new SystemEvent(SystemEvent.Type.START_SERIAL_CONNECTION));
	}

	/**
	 * Initialize the database
	 */
	private void initDatabase()
	{
		DataBaseService.getInstance().init();
	}

	/**
	 * Register the scheduler in the eventbus and load additional jobs
	 */
	private void initScheduler()
	{
		ScheduleService.register();
		EventBusService.post(new SystemEvent(SystemEvent.Type.START_SCHEDULER));

		ScheduleEvent scheduleEvent = new ScheduleEvent(ScheduleEvent.Type.START_RESOURCE_LOG);
		scheduleEvent.getInterval().put(RepeatInterval.SECOND, 120);
		scheduleEvent.setIdentity("resource_check");

		EventBusService.post(scheduleEvent);
		//EventBusService.post(new ScheduleEvent(ScheduleEvent.Type.REBUILD_DATABASE));
	}

	/**
	 * Register the NotificationService in the eventbus
	 */
	private void initNotificationService()
	{
		NotificationService.register();
	}

	private void initExtensions()
	{
		FabLabExtension e = new FabLabExtension();
		e.init();
	}

}
