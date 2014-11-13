
import data.DataBaseHelper;
import data.Log;
import data.SerialConnector;
import protobuf.RblProto;
import server.RBLSocketServer;
import server.RBLWebSocketServer;
import util.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();
    public static Thread serverThread = null;
    public static Thread webServerThread = null;


    public static void main(String[] args){
        Log.add(DEBUG_TAG,"########### RaspberryLife server ###########");
        try {
            Log.add(DEBUG_TAG, "Starting RBLServer on IP: " +
                    InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // RaspberryHome WebSocketServer
        webServerThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG,
                        "Starting WebSocketServer");
                RBLWebSocketServer webServer = new RBLWebSocketServer();
                webServer.start();
            }
        });

        // RaspberryHome Application Server
        serverThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBLSocketServer server = new RBLSocketServer();
                server.start(Config.JAVA_SOCKET_PORT);
            }
        });

        // Start threads
        webServerThread.start();
        serverThread.start();
        // Initialize the serial connector for module communication
        SerialConnector.init();
        DataBaseHelper.init();
        DataBaseHelper.closeConnection();
    }
}
