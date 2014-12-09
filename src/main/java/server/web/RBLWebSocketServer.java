package server.web;

import com.google.common.eventbus.Subscribe;
import event.SystemEvent;
import system.service.EventBusService;
import util.Log;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.handler.StaticFileHandler;
import data.Config;

/**
 * Created by Peter Mösenthin.
 *
 * WebSocketServer class that is created by the main method.
 */
public class RBLWebSocketServer {

    private static RBLWebSocketServer instance = new RBLWebSocketServer();

    private WebServer webServer = null;
    private Thread serverThread = null;

    private static final String DEBUG_TAG = RBLWebSocketServer.class.getSimpleName();

    private RBLWebSocketServer(){
    }

    public static void register(){
        EventBusService.register(instance);
    }

    @Subscribe
    public void handleEvent(SystemEvent e){
        switch (e.getMessage()){
            case SystemEvent.START_WEB_SOCKET_SERVER:
                start();
                break;
            case SystemEvent.STOP_WEB_SOCKET_SERVER:
                break;
            case SystemEvent.RESTART_WEB_SOCKET_SERVER:
                break;
        }
    }

    private void start(){
        serverThread = new Thread(getRunnable());
        serverThread.start();
    }

    private Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                webServer = WebServers.createWebServer(Config.getConf().getInt("socket.web_port"));
                webServer.add(new StaticFileHandler("/static-files"));
                webServer.add("/websocket-echo", new RBLWebSocketHandler());
                webServer.start();
                Log.add(DEBUG_TAG,
                        "WebSocketServer listens on port "
                                + webServer.getPort());
            }
        };
    }
}
