
import data.Log;
import data.MySQLConnection;
import data.SerialConnector;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;
import server.RBHServer;
import util.Config;
import web.RBHWebSocketHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryHome server application.
 */
public class RaspberryHome {

    public static final String DEBUG_TAG = "RaspberryHome";
    public static Thread serverThread = null;
    public static Thread webServerThread = null;

    public static void main(String[] args){
        Log.add(DEBUG_TAG,"RaspberryHome " + Config.VERSION_ID);
        try {
            Log.add(DEBUG_TAG, "Starting RBHServer on IP: " +
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
                WebServer webServer =
                        WebServers.createWebServer(Config.WEBSOCKET_PORT);
                webServer.add(new StaticFileHandler("/static-files"));
                webServer.add("/websocket-echo", new RBHWebSocketHandler());
                webServer.start();
                Log.add(DEBUG_TAG,
                        "WebSocketServer listens on port "
                                + webServer.getPort());

            }
        });
        webServerThread.start();
        // RaspberryHome Application Server
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBHServer server = new RBHServer();
                server.start(Config.JAVA_SOCKET_PORT);
            }
        });
        serverThread.start();

        // Initialize the serialconnector for module communication
        SerialConnector.init();
    }
}
