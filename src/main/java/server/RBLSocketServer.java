package server;

import client.ClientHandler;
import com.google.common.eventbus.Subscribe;
import event.SocketEvent;
import event.SystemEvent;
import system.service.EventBusService;
import data.Config;
import util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Peter Mösenthin.
 *
 * Server class that is created by the main method.
 */
public class RBLSocketServer{

    private static RBLSocketServer instance = new RBLSocketServer();

    private ServerSocket serverAcceptSocket;
    private Thread serverThread = null;

    private boolean acceptClients = true;
    private int port = 0;

    private static final String DEBUG_TAG = RBLSocketServer.class.getSimpleName();

    /**
     * Empty constructor for event access only
     */
    private RBLSocketServer(){
    }

    public static void register() {
        EventBusService.register(instance);
    }

    @Subscribe
    public void handleEvent(SystemEvent e){
        switch (e.getType()){
            case START_SOCKET_SERVER:
                start();
                break;
            case STOP_SOCKET_SERVER:
                stop();
                break;
            case RESTART_SOCKET_SERVER:
                break;
        }
    }

    private void start(){
        Log.add(DEBUG_TAG, "Starting...");
        this.port = Config.getConf().getInt("socket.java_port");
        serverThread = new Thread(getRunnable());
        serverThread.start();
    }

    private void stop(){
        Log.add(DEBUG_TAG, "Stopping...");
        acceptClients = false;
        if(serverThread != null){
            serverThread.interrupt();
        }
    }

    private void restart(){
        stop();
        start();
    }

    private Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                try {
                    serverAcceptSocket = new ServerSocket(port);
                    Log.add(DEBUG_TAG, "Server listens on port " + port);
                    while(acceptClients) {
                        // Accept
                        SocketEvent e = new SocketEvent(
                                SocketEvent.Type.SOCKET_ACCEPT,
                                serverAcceptSocket.accept());
                        EventBusService.post(e);
                        Log.add(DEBUG_TAG, "Client connected on port " + port);
                    }
                } catch (IOException e) {
                    Log.add(DEBUG_TAG, "Server problem. Please restart.", e);
                } finally {
                    EventBusService.post(
                            new SystemEvent(SystemEvent.Type.CLOSE_SOCKET_CONNECTIONS));
                }
            }
        };
    }

}
