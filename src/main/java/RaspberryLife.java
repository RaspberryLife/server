
import system.SystemManager;


/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();

    private static SystemManager systemManager = SystemManager.getInstance();

    public static void main(String[] args){
        systemManager.start();
    }
}
