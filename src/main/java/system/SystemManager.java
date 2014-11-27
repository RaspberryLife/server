package system;


import data.DataBaseHelper;
import scheduling.ScheduleManager;
import server.RBLSocketServer;
import server.web.RBLWebSocketServer;
import server.serial.SerialConnector;
import util.Config;
import util.Log;
import util.NetworkUtil;

public class SystemManager {

    private static SystemManager instance = null;

    public static final String DEBUG_TAG = SystemManager.class.getSimpleName();

    RBLSocketServer socketServer = null;
    public static Thread webServerThread = null;
    public static SerialConnector serialConnector = null;
    public static InstructionHandler instructionHandler;


    public static boolean runDebugSetup = false;


    public static SystemManager getInstance(){
        if(instance == null){
            return instance = new SystemManager();
        } else {
            return instance;
        }
    }

    private SystemManager(){

    }

    public void start(){
        Log.printLogHeader();
        initConfig();
        NetworkUtil.listIPAddresses();
        initEventBus();
        instructionHandler = new InstructionHandler();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        if(runDebugSetup){
            initDatabase();
            initScheduler();
        }
    }

    public void stop(){
        Log.add(DEBUG_TAG, "Stopping ... (But not really)");
    }

    public void restart(){
        stop();
        start();
    }

    public InstructionHandler getInstructionHandler() {
        return instructionHandler;
    }

    //----------------------------------------------------------------------------------------------
    //                                      STARTUP
    //----------------------------------------------------------------------------------------------

    private void initConfig(){
        Config.readConfig();
        runDebugSetup = Config.getConf().getBoolean("test.run_debug");
        //Config.dumpConfig();
    }


    private void startSocketServer(){
        Log.add(DEBUG_TAG, "Starting Java socket server");
        RBLSocketServer server = new RBLSocketServer();
        server.start();
    }

    private void startWebSocketServer(){
        Log.add(DEBUG_TAG, "Starting WebSocketServer");
        RBLWebSocketServer webServer = new RBLWebSocketServer();
        webServer.start();
    }

    // Initialize the serial connector for module communication
    private void initSerialConnection(){
        Log.add(DEBUG_TAG, "Initializing serial connector");
        serialConnector = new SerialConnector();
        serialConnector.init();
    }

    private void initDatabase(){
        Log.add(DEBUG_TAG, "Initializing database");
        DataBaseHelper.setUpInitial();
    }

    private void initScheduler(){
        Log.add(DEBUG_TAG, "Initializing scheduler");
        ScheduleManager sm = new ScheduleManager();
        sm.test();
    }

    private void initEventBus() {
        EventBusService.init();
    }



}
