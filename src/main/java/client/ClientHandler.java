package client;

import data.Log;
import interfaces.ConnectionListener;
import org.webbitserver.WebSocketConnection;
import message.RBHproto.RBHMessage;

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

    public static final String DEBUG_TAG = "ClientHandler";


    /**
     * Handles a regular Java socket as incoming connection.
     * @param clientSocket
     */
    public void handleSocketClient(final Socket clientSocket){
        Thread handleThread = new Thread(new Runnable() {
            @Override
            public void run() {
            final RaspberryLifeClient client =
                    new SocketClient(clientSocket);
            setUpConnectionListener(client);
            clientList.add(client);
            }
        });
        handleThread.start();
    }


    /**
     * Sets the connection listener that will add/remove the client to
     * ClientHandlers client list.
     * @param client
     */
    private void setUpConnectionListener(final RaspberryLifeClient client){
        client.setConnectionListener(new ConnectionListener() {
            @Override
            public void denied(String reason) {
                Log.add(DEBUG_TAG,
                        "Client denied."
                                + " ID=" + client.getId()
                                + " REASON=" + reason
                );
                clientList.remove(client);
            }

            @Override
            public void accepted() {
                Log.add(DEBUG_TAG,
                        "Client accepted with ID="
                                + client.getId()
                );
            }

            @Override
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
     * Handle a websocket as incoming connection.
     * @param connection
     */
    public void handleWebSocketClient(final WebSocketConnection connection){
        final RaspberryLifeClient client = new WebSocketClient(connection);
        setUpConnectionListener(client);
        clientList.add(client);
    }

    /**
     * Finds and returns a connection from the clientlist
     * by comparing the hashcode of the connection.
     * @param connection
     * @return
     */
    public WebSocketClient getWebSocketClient(WebSocketConnection connection){
        for(RaspberryLifeClient client : clientList){
            if(client instanceof WebSocketClient){
               WebSocketConnection clientConn =
                       ((WebSocketClient) client).getWebSocketConnection();
                if(clientConn.hashCode() == connection.hashCode()){
                    return (WebSocketClient) client;
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
    public static void broadcastMessage(RBHMessage message){
        for(RaspberryLifeClient client : clientList){
           client.sendMessage(message);
        }
    }
}
