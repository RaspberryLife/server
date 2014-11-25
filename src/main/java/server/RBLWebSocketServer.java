package server;

import util.Log;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;
import util.Config;

/**
 * Created by Peter Mösenthin.
 *
 * WebSocketServer class that is created by the main method.
 */
public class RBLWebSocketServer implements Runnable{

    private WebServer webServer = null;
    private Thread serverThread = null;

    private static final String DEBUG_TAG = RBLWebSocketServer.class.getSimpleName();

    public void start(){
        serverThread = new Thread(this);
        serverThread.start();
    }

    public WebServer getWebServer(){
        return webServer;
    }


    public void run() {
        webServer = WebServers.createWebServer(Config.getConf().getInt("socket.web_port"));
        webServer.add(new StaticFileHandler("/static-files"));
        webServer.add("/websocket-echo", new RBLWebSocketHandler());
        webServer.start();
        Log.add(DEBUG_TAG,
                "WebSocketServer listens on port "
                        + webServer.getPort());
    }
}
