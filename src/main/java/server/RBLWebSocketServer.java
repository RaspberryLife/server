package server;

import data.Log;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;
import util.Config;

/**
 * Created by Peter Mösenthin.
 *
 * WebSocketServer class that is created by the main method.
 */
public class RBLWebSocketServer {

    private WebServer webServer = null;

    private static final String DEBUG_TAG = RBLWebSocketServer.class.getSimpleName();

    public void start(){
        webServer = WebServers.createWebServer(Config.WEBSOCKET_PORT);
        webServer.add(new StaticFileHandler("/static-files"));
        webServer.add("/websocket-echo", new RBLWebSocketHandler());
        webServer.start();
        Log.add(DEBUG_TAG,
                "WebSocketServer listens on port "
                        + webServer.getPort());
    }

    public WebServer getWebServer(){
        return webServer;
    }


}
