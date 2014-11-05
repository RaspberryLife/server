
import data.Log;
import data.SerialConnector;
import server.RBLServer;
import server.RBLWebSocketServer;
import util.Config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryHome server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = "RaspberryHome";
    public static Thread serverThread = null;
    public static Thread webServerThread = null;

    public static void main(String[] args){
        Log.add(DEBUG_TAG,"RaspberryHome " + Config.VERSION_ID);
        try {
            Log.add(DEBUG_TAG, "Starting RBLServer on IP: " +
                    InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // RaspberryHome WebSocketServer
        webServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.add(DEBUG_TAG,
                        "Starting WebSocketServer");
                RBLWebSocketServer webServer = new RBLWebSocketServer();
                webServer.start();
            }
        });

        // RaspberryHome Application Server
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBLServer server = new RBLServer();
                server.start(Config.JAVA_SOCKET_PORT);
            }
        });

        // Start threads
        webServerThread.start();
        serverThread.start();
        // Initialize the serial connector for module communication
        SerialConnector.init();
    }
}
