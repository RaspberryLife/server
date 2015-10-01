package rbl.system;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import rbl.data.Config;
import rbl.event.ScheduleEvent;
import rbl.event.SystemEvent;
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
	static Logger log = Logger.getLogger(SystemManager.class.getName());

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
//		ClassLoader classLoader = Config.class.getClassLoader();
//		// or use:
//		// ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		// depending on what's appropriate in your case.
//		Enumeration<URL> roots = null;
//		try
//		{
//			roots = classLoader.getResources("log4j.properties");
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		while (roots.hasMoreElements()) {
//			final URL url = roots.nextElement();
//			Log.add(DEBUG_TAG, url.toString());
//			log.info(url.toString());
//			try {
//				PropertyConfigurator.configure(url.toString());
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//		}
//		try {
//			Log.add(DEBUG_TAG, getClass().getClassLoader().getResource("resources/log4j.properties").toString());
//			File file = new File(getClass().getClassLoader().getResource("log4j.properties").getFile());
//			PropertyConfigurator.configure(file.getAbsolutePath());
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		//Log.add(DEBUG_TAG, );
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
		//FabLabExtension e = new FabLabExtension();
		//e.init();
	}

}
