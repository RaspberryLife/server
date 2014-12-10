
import event.SystemEvent;
import system.SystemManager;
import system.service.EventBusService;


/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();

    public static void main(String[] args){
        SystemManager.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SYSTEM));
    }

}
