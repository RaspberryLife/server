package client;

import interfaces.ConnectionListener;
import message.RBHproto;

/**
 * Created by Peter Mösenthin.
 *
 * Abstract class for Client connections.
 */
public abstract class RaspberryLifeClient {

    private String id = "";
    public boolean isAccepted = false;
    private ConnectionListener connectionListener;


    //==========================================================================
    // Getter & Setter
    //==========================================================================


    public void setId(String id) {
        if(this.id == null || this.id == "") {
            this.id = id;
        }
    }

    public String getId() {
        return id;
    }

    public void setConnectionListener(ConnectionListener listener){
        if(listener != null) {
            this.connectionListener = listener;
        }
    }

    public ConnectionListener getConnectionListener(){
        return connectionListener;
    }

    //==========================================================================
    // Connection state handling
    //==========================================================================

    /**
     * Abstract method to perform operations to close the client connection.
     */
    protected abstract void onConnectionClosed();

    /**
     * Abstract method to perform operations if the connection is denied.
     * E.g. bad authentication
     * @param reason
     */
    protected abstract void onConnectionDenied(String reason);

    /**
     * Abstract method to perform operations to accept the connection.
     */
    protected abstract void onConnectionAccepted();


    /**
     * Perform operations to close the client connection.
     */
    public void closeConnection(){
        if(connectionListener != null) {
            connectionListener.closed();
        }
        onConnectionClosed();
    }

    /**
     * Perform operations if the connection is denied. E.g. bad authentication
     * @param reason
     */
    public void denyConnection(String reason){
        if(connectionListener != null) {
            connectionListener.denied(reason);
        }
        onConnectionDenied(reason);
    }

    /**
     * Perform operations to accept the connection.
     */
    public void acceptConnection(){
        isAccepted = true;
        if(connectionListener != null) {
            connectionListener.accepted();
        }
        onConnectionAccepted();
    }

    //==========================================================================
    // Message handling
    //==========================================================================

    /**
     * Sends a message over the network.
     * @param message
     */
    public abstract void sendMessage(RBHproto.RBHMessage message);

    //public abstract void readMessage(RBHproto.RBHMessage message);
}
