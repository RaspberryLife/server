package server;

import client.ClientHandler;
import system.Config;
import util.Log;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Peter Mösenthin.
 *
 * Server class that is created by the main method.
 */
public class RBLSocketServer implements Runnable{

    private ServerSocket serverAcceptSocket;
    private Thread serverThread = null;

    private boolean acceptClients = true;
    private int port = 0;
    private boolean serverRunning = false;
    private ClientHandler clientHandler = ClientHandler.getInstance();

    private static final String DEBUG_TAG = RBLSocketServer.class.getSimpleName();

    public void start(){
        this.port = Config.getConf().getInt("socket.java_port");
        serverThread = new Thread(this);
        serverThread.start();
    }

    public void stop(){
        serverRunning = false;
        acceptClients = false;
    }


    public boolean isRunning(){
        return serverRunning;
    }

    public void run() {
        serverRunning = true;
        try {
            serverAcceptSocket = new ServerSocket(port);
            Log.add(DEBUG_TAG, "Server listens on port " + port);
            while(acceptClients) {
                // Accept
                clientHandler.handleSocketClient(serverAcceptSocket.accept());
                Log.add(DEBUG_TAG, "Client connected on port " + port);
            }
        } catch (IOException e) {
            serverRunning = false;
            Log.add(DEBUG_TAG, "Server problem. Please restart", e);
        } finally {
            clientHandler.closeAllConnections();
        }
    }
}
