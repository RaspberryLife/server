package system;


import system.service.ClientService;
import com.google.common.eventbus.Subscribe;
import event.*;
import system.service.DataBaseService;
import system.service.EventBusService;
import system.service.NotificationService;
import system.service.ScheduleService;
import server.RBLSocketServer;
import server.web.RBLWebSocketServer;
import server.serial.SerialConnector;
import data.Config;
import util.Log;
import util.NetworkUtil;

/**
 * Created by Peter Mösenthin.
 */
public class SystemManager {

    public static final String DEBUG_TAG = SystemManager.class.getSimpleName();

    private static SystemManager instance = new SystemManager();

    public static void register(){
        EventBusService.register(instance);
    }

    /**
     * Private constructor for event access only
     */
    private SystemManager(){
    }

    @Subscribe
    public void handleEvent(SystemEvent e){
        switch (e.getType()){
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
     * Starts the server 
     */
    private void start(){
        Log.printLogHeader();
        // 1. Load config
        loadConfig();
        // 2. Init event bus
        initEventBus();
        // 3. Do the rest
        initNotificationService();
        NetworkUtil.listIPAddresses();
        ClientService.register();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        initDatabase();
        initScheduler();
    }

    private void stop(){
        Log.add(DEBUG_TAG, "Stopping ... (But not really)");
    }

    private void restart(){
        stop();
        start();
    }

    //----------------------------------------------------------------------------------------------
    //                                      STARTUP
    //----------------------------------------------------------------------------------------------

    /**
     * Load the server configurtion
     */
    private void loadConfig(){
        Log.add(DEBUG_TAG, "Loading configuration");
        Config.readConfig();
        //Config.dumpConfig();
    }

    /**
     * Start the eventbus service
     */
    private void initEventBus() {
        EventBusService.init();
    }
    
    /**
     * start the socket server
     */
    private void startSocketServer(){
        RBLSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SOCKET_SERVER));
    }

    /**
     * Register and start the websocket server in the eventbus
     */
    private void startWebSocketServer(){
        RBLWebSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_WEB_SOCKET_SERVER));
    }
    
    /**
     * Register the serial connector in the eventbus for module communication
     */
    private void initSerialConnection(){
        SerialConnector.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SERIAL_CONNECTION));
    }

    /**
     * Initialize the database
     */
    private void initDatabase(){
        DataBaseService.getInstance().init();
    }

    /**
     * Register the scheduler in the eventbus and load additional jobs
     */
    private void initScheduler(){
        ScheduleService.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SCHEDULER));
        EventBusService.post(new ScheduleEvent(
                "resource_check",120,
                ScheduleEvent.Type.START_RESOURCE_LOG)
        );
        EventBusService.post(new ScheduleEvent(ScheduleEvent.Type.REBUILD_DATABASE));
    }

    /**
     * Register the NotificationService in the eventbus
     */
    private void initNotificationService() {
        NotificationService.register();
    }

}
