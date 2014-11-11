package server;

import client.ClientHandler;
import data.Log;
import util.Config;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Peter Mösenthin.
 *
 * Server class that is created by the main method.
 */
public class RBLSocketServer {

    private ServerSocket serverAcceptSocket;

    private boolean acceptClients = true;
    private int port = 0;
    private boolean serverRunning = false;
    private ClientHandler clientHandler = ClientHandler.getInstance();

    private static final String DEBUG_TAG = RBLSocketServer.class.getSimpleName();

    public void start(int port){
        serverRunning = true;
        this.port = port;
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
            Log.add(DEBUG_TAG, "Server problem. Please restart");
            e.printStackTrace();
        } finally {
            clientHandler.closeAllConnections();
        }
    }

    public boolean isRunning(){
        return serverRunning;
    }

    public void setAcceptClients(boolean acceptClients){
        this.acceptClients = acceptClients;
    }
}
