package system.service;

import client.RBLSocketClient;
import client.RBLWebSocketClient;
import client.RaspberryLifeClient;
import com.google.common.eventbus.Subscribe;
import data.Config;
import event.NotificationEvent;
import event.SocketEvent;
import event.SystemEvent;
import event.WebSocketEvent;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import util.Log;
import client.ConnectionListener;
import org.webbitserver.WebSocketConnection;
import protobuf.RblProto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 *
 * The ClientService is responsible for organizing connected clients with
 * websockets  as well as regular Java sockets.
 * All clients must be subtypes of RaspberryHomeClient.
 */
public class ClientService {

    public static final String DEBUG_TAG = ClientService.class.getSimpleName();

    private static List<RaspberryLifeClient> clientList = new ArrayList<RaspberryLifeClient>();
    private static ClientService instance = new ClientService();

    //----------------------------------------------------------------------------------------------
    //                                      LIFECYCLE
    //----------------------------------------------------------------------------------------------

    /**
     * Private constructor for event access only
     */
    private ClientService(){
    }

    public static void register(){
        EventBusService.register(instance);
    }

    //----------------------------------------------------------------------------------------------
    //                                      EVENT RECEIVER
    //----------------------------------------------------------------------------------------------

    @Subscribe
    public void handleSystemEvent(SystemEvent e){
        switch(e.getType()){
            case CLOSE_ALL_CONNECTIONS:
                closeAllConnections();
                break;
            case CLOSE_SOCKET_CONNECTIONS:
                closeSocketConnections();
                break;
            case CLOSE_WEB_SOCKET_CONNECTIONS:
                closeWebSocketConnections();
                break;
        }
    }

    @Subscribe
    public void handleWebSocketEvent(WebSocketEvent e){
        switch (e.getType()){
            case CONNECTION_OPEN:
                handleWebSocketClient(e);
                break;
            case CONNECTION_CLOSE:
                closeWebSocketClient(e);
                break;
            case MESSAGE:
                deliverWebSocketMessage(e);
                break;
        }
    }

    @Subscribe
    public void handleSocketEvent(SocketEvent e){
        switch (e.getType()){
            case SOCKET_ACCEPT:
                handleSocketClient(e);
                break;
        }
    }

    @Subscribe
    public void handleNotificationEvent(NotificationEvent e){
        if(e.getType() == NotificationEvent.Type.CLIENT_BROADCAST){
            broadcastMessage(e);
        }
    }


    //----------------------------------------------------------------------------------------------
    //                                      WEB SOCKET HANDLING
    //----------------------------------------------------------------------------------------------

    private void closeWebSocketClient(WebSocketEvent e){
        RBLWebSocketClient c = getWebSocketClient(e);
        c.closeConnection();
    }

    private void closeWebSocketConnections() {
        for(RaspberryLifeClient client : clientList){
            if(client instanceof RBLWebSocketClient){
                client.closeConnection();
            }
        }
    }

    /**
     * Handle a web socket as incoming connection.
     * @param e
     */
    private void handleWebSocketClient(final WebSocketEvent e){
        final RaspberryLifeClient client = new RBLWebSocketClient(e.getConnection());
        setUpConnectionListener(client);
        clientList.add(client);
    }

    private void deliverWebSocketMessage(WebSocketEvent e){
        RBLWebSocketClient c = getWebSocketClient(e);
        if(c != null){
            c.readMessage(e.getMessage());
        }else {
            Log.add(DEBUG_TAG,
                    "Unable to deliver message to receiving connection. " +
                            "Client connection not found.");
        }
    }

    /**
     * Finds and returns a connection from the client list
     * by comparing the hashcode of the connection.
     * @param e
     * @return
     */
    private RBLWebSocketClient getWebSocketClient(WebSocketEvent e){
        for(RaspberryLifeClient client : clientList){
            if(client instanceof RBLWebSocketClient){
                WebSocketConnection clientConn =
                        ((RBLWebSocketClient) client).getWebSocketConnection();
                if(clientConn.hashCode() == e.getConnection().hashCode()){
                    return (RBLWebSocketClient) client;
                }
            }
        }
        return null;
    }


    //----------------------------------------------------------------------------------------------
    //                                      SOCKET HANDLING
    //----------------------------------------------------------------------------------------------


    /**
     * Handles a regular Java socket as incoming connection.
     * @param e
     */
    private void handleSocketClient(final SocketEvent e){
        Thread handleThread = new Thread(new Runnable() {
            public void run() {
            final RaspberryLifeClient client =
                    new RBLSocketClient(e.getSocket());
            setUpConnectionListener(client);
            clientList.add(client);
            }
        });
        handleThread.start();
    }

    private void closeSocketConnections() {
        for(RaspberryLifeClient client : clientList){
            if(client instanceof RBLSocketClient){
                client.closeConnection();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    //                                      GENERIC
    //----------------------------------------------------------------------------------------------



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
     * Closes all connections of all clients in the clientlist
     */
    private void closeAllConnections() {
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
    private static boolean isAuthenticated(String clientID){
        return getClientWithID(clientID) != null;
    }


    /**
     * Returns the client with the specified id.
     * @param id
     * @return
     */
    private static RaspberryLifeClient getClientWithID(String id){
        for(RaspberryLifeClient client : clientList){
            if(client.getId().equals(id)){
                return client;
            }
        }
        return null;
    }

    /**
     * Sends a message to all clients
     * @param e
     */
    private static void broadcastMessage(NotificationEvent e){
        RBLMessage m = ProtoFactory.buildPlainTextMessage(
                Config.getConf().getString("server.id"),
                RblProto.RBLMessage.MessageFlag.RESPONSE,
                ProtoFactory.buildPlainText("Serial connector received message: " + e.getMessage()
                ));
        for(RaspberryLifeClient client : clientList){
           client.sendMessage(m);
        }
    }

}
