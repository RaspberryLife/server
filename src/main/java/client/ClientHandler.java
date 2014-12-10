package client;

import com.google.common.eventbus.Subscribe;
import event.NewClientEvent;
import event.SystemEvent;
import event.WebSocketEvent;
import event.WebSocketMessageEvent;
import system.service.EventBusService;
import util.Log;
import interfaces.ConnectionListener;
import org.webbitserver.WebSocketConnection;
import protobuf.RblProto.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 *
 * The ClientHandler is responsible for organizing connected clients with
 * websockets  as well as regular Java sockets.
 * All clients must be subtypes of RaspberryHomeClient.
 */
public class ClientHandler {

    private static List<RaspberryLifeClient> clientList = new ArrayList<RaspberryLifeClient>();

    private static ClientHandler instance = new ClientHandler();

    public static final String DEBUG_TAG = ClientHandler.class.getSimpleName();


    private ClientHandler(){
    }

    @Subscribe
    public void handleNewClientEvent(NewClientEvent e){

    }

    @Subscribe
    public void handleSystemEvent(SystemEvent e){

    }

    @Subscribe
    public void handleWebSocketEvent(WebSocketEvent e){
        switch (e.getType()){
            case TYPE_CONNECTION_OPEN:
                break;
            case TYPE_CONNECTION_CLOSE:
                break;
            case TYPE_MESSAGE:
                break;
        }

        RBLWebSocketClient c = getWebSocketClient(e.getConnection());
        if(c != null){
            c.readMessage(e.getMessage());
        }else {
            Log.add(DEBUG_TAG,
                    "Unable to deliver message to receiving connection. " +
                            "Client not connection found.");
        }
    }

    public static void register(){
        EventBusService.register(instance);
    }


    /**
     * Handles a regular Java socket as incoming connection.
     * @param clientSocket
     */
    public void handleSocketClient(final Socket clientSocket){
        Thread handleThread = new Thread(new Runnable() {
            public void run() {
            final RaspberryLifeClient client =
                    new RBLSocketClient(clientSocket);
            setUpConnectionListener(client);
            clientList.add(client);
            }
        });
        handleThread.start();
    }

    /**
     * Handle a websocket as incoming connection.
     * @param connection
     */
    public void handleWebSocketClient(final WebSocketConnection connection){
        final RaspberryLifeClient client = new RBLWebSocketClient(connection);
        setUpConnectionListener(client);
        clientList.add(client);
    }



    /**
     * Sets the connection listener that will add/remove the client to
     * ClientHandlers client list.
     * @param client
     */
    private void setUpConnectionListener(final RaspberryLifeClient client){
        client.setConnectionListener(new ConnectionListener() {
            public void denied(String reason) {
                Log.add(DEBUG_TAG,
                        "Client denied."
                                + " ID=" + client.getId()
                                + " REASON=" + reason
                );
                clientList.remove(client);
            }

            public void accepted() {
                Log.add(DEBUG_TAG,
                        "Client accepted with ID="
                                + client.getId()
                );
            }

            public void closed() {
                Log.add(DEBUG_TAG,
                        "Removing client after closed connection "
                                + "ID=" + client.getId()
                );
                clientList.remove(client);
            }
        });
    }

    /**
     * Finds and returns a connection from the clientlist
     * by comparing the hashcode of the connection.
     * @param connection
     * @return
     */
    public RBLWebSocketClient getWebSocketClient(WebSocketConnection connection){
        for(RaspberryLifeClient client : clientList){
            if(client instanceof RBLWebSocketClient){
               WebSocketConnection clientConn =
                       ((RBLWebSocketClient) client).getWebSocketConnection();
                if(clientConn.hashCode() == connection.hashCode()){
                    return (RBLWebSocketClient) client;
                }
            }
        }
        return null;
    }

    /**
     * Closes all connections of all clients in the clientlist
     */
    public void closeAllConnections() {
        for(RaspberryLifeClient client : clientList){
            client.closeConnection();
        }
    }

    /**
     * Checks if a client is already authenticated
     * @param clientID ID of the client
     * @return true if the client is in the clientlist and therefore
     * authenticated.
     */
    public static boolean isAuthenticated(String clientID){
        return getClientWithID(clientID) != null;
    }


    /**
     * Returns the client with the specified id.
     * @param id
     * @return
     */
    public static RaspberryLifeClient getClientWithID(String id){
        for(RaspberryLifeClient client : clientList){
            if(client.getId().equals(id)){return client;}
        }
        return null;
    }

    /**
     * Sends a message to all clients
     * @param message
     */
    public static void broadcastMessage(RBLMessage message){
        for(RaspberryLifeClient client : clientList){
           client.sendMessage(message);
        }
    }
}
