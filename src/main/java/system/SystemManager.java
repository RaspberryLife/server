package system;


import com.google.common.eventbus.Subscribe;
import data.DataBaseManager;
import event.SystemEvent;
import system.service.EventBusService;
import protobuf.ProtobufInstructionResolver;
import system.service.ScheduleService;
import server.RBLSocketServer;
import server.web.RBLWebSocketServer;
import server.serial.SerialConnector;
import util.Config;
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
     * Empty constructor for event access only
     */
    private SystemManager(){
    }

    @Subscribe
    public void handleEvent(SystemEvent e){
        switch (e.getMessage()){
            case SystemEvent.START_SYSTEM:
                start();
                break;
            case SystemEvent.STOP_SYSTEM:
                stop();
                break;
            case SystemEvent.RESTART_SYSTEM:
                restart();
                break;
        }
    }

    private void start(){
        Log.printLogHeader();
        loadConfig();
        NetworkUtil.listIPAddresses();
        initEventBus();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        if(runDebugSetup){
            Log.add(DEBUG_TAG, "Running debug setup");
            //initDatabase();
            //initScheduler();
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


    private void startSocketServer(){
        Log.add(DEBUG_TAG, "Starting RBLSocketServer");
        RBLSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.START_SOCKET_SERVER));
    }

    private void startWebSocketServer(){
        Log.add(DEBUG_TAG, "Starting RBLWebSocketServer");
        RBLWebSocketServer.register();
        EventBusService.post(new SystemEvent(SystemEvent.START_WEB_SOCKET_SERVER));
    }

    // Initialize the serial connector for module communication
    private void initSerialConnection(){
        Log.add(DEBUG_TAG, "Initializing serial connector");
        SerialConnector.register();
        EventBusService.post(new SystemEvent(SystemEvent.START_SERIAL_CONNECTION));
    }

    private void initDatabase(){
        Log.add(DEBUG_TAG, "Initializing database");
        DataBaseManager dbm = new DataBaseManager();
        dbm.initSession();
        dbm.runHibernateWriteTest();
        dbm.runHibernateReadTest();
    }

    private void initScheduler(){
        Log.add(DEBUG_TAG, "Initializing scheduler");
        ScheduleService sm = new ScheduleService();
        sm.test();
    }

    private void initEventBus() {
        EventBusService.init();
    }

}
