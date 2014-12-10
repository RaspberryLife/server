package system;


import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import event.DataBaseEvent;
import event.ScheduleEvent;
import system.service.DataBaseService;
import event.SystemEvent;
import system.service.EventBusService;
import system.service.ScheduleService;
import server.RBLSocketServer;
import server.web.RBLWebSocketServer;
import server.serial.SerialConnector;
import data.Config;
import util.Log;
import util.NetworkUtil;

public class SystemManager {

    private static SystemManager instance = new SystemManager();

    public static final String DEBUG_TAG = SystemManager.class.getSimpleName();

    public static boolean runDebugSetup;

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

    private void start(){
        Log.printLogHeader();
        // 1. Log load config
        loadConfig();
        // 2. Init event bus
        initEventBus();
        // 3. Do the rest
        NetworkUtil.listIPAddresses();
        ClientHandler.register();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        initScheduler();
        if(runDebugSetup){
            Log.add(DEBUG_TAG, "Running debug setup");
            //initDatabase();
        }
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

    private void loadConfig(){
        Log.add(DEBUG_TAG, "Loading configuration");
        Config.readConfig();
        runDebugSetup = Config.getConf().getBoolean("test.run_debug");
        //Config.dumpConfig();
    }

    private void initEventBus() {
        EventBusService.init();
    }


    private void startSocketServer(){
        RBLSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SOCKET_SERVER));
    }

    private void startWebSocketServer(){
        RBLWebSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_WEB_SOCKET_SERVER));
    }

    // Initialize the serial connector for module communication
    private void initSerialConnection(){
        SerialConnector.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SERIAL_CONNECTION));
    }

    private void initDatabase(){
        DataBaseService.register();
        EventBusService.post(new DataBaseEvent(DataBaseEvent.START_SESSION));
        EventBusService.post(new DataBaseEvent(DataBaseEvent.RUN_TEST));
    }

    private void initScheduler(){
        ScheduleService.register();
        EventBusService.post(new SystemEvent(SystemEvent.Type.START_SCHEDULER));
        EventBusService.post(new ScheduleEvent(
                "resource_check",120,
                ScheduleEvent.Type.START_RESOURCE_LOG)
        );
        EventBusService.post(new ScheduleEvent(
                        "time_log",600,
                        ScheduleEvent.Type.START_TIME_LOG)
        );
    }

}
