
import data.DataBaseHelper;
import util.Log;
import data.SerialConnector;
import scheduling.ScheduleManager;
import server.RBLSocketServer;
import server.RBLWebSocketServer;
import util.Config;
import util.NetworkUtil;



/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();
    public static Thread serverThread = null;
    public static Thread webServerThread = null;

    public static boolean runDebugSetup = false;

    //----------------------------------------------------------------------------------------------
    //                                      MAIN
    //----------------------------------------------------------------------------------------------

    public static void main(String[] args){
        Log.printLogHeader();
        Config.readConfig();
        //Config.dumpConfig();
        NetworkUtil.listIPAddresses();
        startSocketServer();
        startWebSocketServer();
        initSerialConnection();
        initDatabase();
        if(runDebugSetup){
            initScheduler();
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      STARTUP
    //----------------------------------------------------------------------------------------------

    public static void startSocketServer(){
        serverThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBLSocketServer server = new RBLSocketServer();
                server.start(Config.get().getInt("socket.java_port"));
            }
        });
        serverThread.start();
    }

    public static void startWebSocketServer(){
        webServerThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG,
                        "Starting WebSocketServer");
                RBLWebSocketServer webServer = new RBLWebSocketServer();
                webServer.start();
            }
        });
        webServerThread.start();
    }

    // Initialize the serial connector for module communication
    public static void initSerialConnection(){
        SerialConnector.init();
    }

    public static void initDatabase(){
        Log.add(DEBUG_TAG, "Initializing database");
        DataBaseHelper.setUpInitial();
    }

    public static void initScheduler(){
        ScheduleManager sm = new ScheduleManager();
        sm.test();
    }

    public static void initGoogleCalendar(){

    }

}
