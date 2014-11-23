package system;


import com.google.common.eventbus.EventBus;
import data.DataBaseHelper;
import scheduling.ScheduleManager;
import server.RBLSocketServer;
import server.RBLWebSocketServer;
import server.SerialConnector;
import util.Config;
import util.Log;
import util.NetworkUtil;

public class SystemManager {

    private static SystemManager instance = null;

    public static final String DEBUG_TAG = SystemManager.class.getSimpleName();

    public static Thread serverThread = null;
    public static Thread webServerThread = null;
    public static SerialConnector serialConnector = null;
    public static InstructionHandler instructionHandler;


    public static boolean runDebugSetup = true;


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
        Log.add(DEBUG_TAG, "Starting ...");
        Log.printLogHeader();
        Config.readConfig();
        //Config.dumpConfig();
        NetworkUtil.listIPAddresses();
        initEventBus();
        instructionHandler = new InstructionHandler();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        initDatabase();
        if(runDebugSetup){
            initScheduler();
        }
    }

    public void stop(){
        Log.add(DEBUG_TAG, "Stopping ...");
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



    private static void startSocketServer(){
        serverThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBLSocketServer server = new RBLSocketServer();
                server.start(Config.getConf().getInt("socket.java_port"));
            }
        });
        serverThread.start();
    }

    private static void startWebSocketServer(){
        Log.add(DEBUG_TAG, "Starting WebSocketServer");
        webServerThread = new Thread(new Runnable() {
            public void run() {
                RBLWebSocketServer webServer = new RBLWebSocketServer();
                webServer.start();
            }
        });
        webServerThread.start();
    }

    // Initialize the serial connector for module communication
    private static void initSerialConnection(){
        Log.add(DEBUG_TAG, "Initializing serial connector");
        serialConnector = new SerialConnector();
        serialConnector.init();
    }

    private static void initDatabase(){
        Log.add(DEBUG_TAG, "Initializing database");
        DataBaseHelper.setUpInitial();
    }

    private static void initScheduler(){
        Log.add(DEBUG_TAG, "Initializing scheduler");
        ScheduleManager sm = new ScheduleManager();
        sm.test();
    }

    private void initEventBus() {
        EventBusService.init();
    }



}