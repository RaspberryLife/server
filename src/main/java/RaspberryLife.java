
import event.SystemEvent;
import system.SystemManager;
import system.service.EventBusService;
import util.Log;


/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();

    public static void main(String[] args){
		Log.init();
		SystemManager.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SYSTEM));
    }

}
