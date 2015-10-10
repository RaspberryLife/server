package rbl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rbl.event.SystemEvent;
import rbl.system.SystemManager;
import rbl.event.EventBusService;
import rbl.util.Log;

/**
 * Created by Peter Mösenthin.
 * <p>
 * Main class for the RaspberryLife serial application.
 */
@SpringBootApplication
public class RaspberryLife
{

	public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();

	public static void main(String[] args)
	{
		SpringApplication.run(RaspberryLife.class, args);
		Log.init();
		SystemManager.register();
		EventBusService.post(new SystemEvent(SystemEvent.Type.START_SYSTEM));
	}

}
