package client;

import data.Log;
import protobuf.ProtoFactory;
import protobuf.ProtobufMessageHandler;
import protobuf.RBHproto;
import util.Config;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Peter Mösenthin.
 *
 * This is the representaion of a RaspberryHomeClient using a Java socket
 * connection.
 */
public class DirectSocketClient extends RaspberryHomeClient {

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    protected final ProtobufMessageHandler messageHandler =
            new ProtobufMessageHandler(this);
    public static final String DEBUG_TAG = "DirectSocketClient";
    private Thread readThread = null;



    public DirectSocketClient(Socket socket){
        this.socket = socket;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            Log.add(DEBUG_TAG,
                    "Client: Could not set up network streams");
            e.printStackTrace();
        }
        startReadThread();
    }


    //==========================================================================
    // Message handling
    //==========================================================================

    /**
     * Initializes a thread to read incoming messages.
     */
    public void startReadThread(){
        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                RBHproto.RBHMessage message;
                boolean read = true;
                while (read) {
                    try {
                        if ((message =  (RBHproto.RBHMessage)
                                inputStream.readObject()) != null) {
                            if(getId().isEmpty()) {
                                // Do nothing
                            }else{
                                Log.add(DEBUG_TAG,
                                        "Client received message " +
                                                "ID=" + getId()
                                );
                            }
                            messageHandler.handleMessage(message);
                        }
                    } catch (IOException e) {
                        read = false;
                        Log.add(DEBUG_TAG,
                                "Client could not read message " +
                                        "ID=" + getId()
                        );
                        readThread.interrupt();
                        if(e.getMessage().equalsIgnoreCase("Connection reset")){
                            closeConnection();
                            Log.add(DEBUG_TAG,
                                    "Client reset connection " +
                                            "ID=" + getId()
                            );
                        }
                        //e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        Log.add(DEBUG_TAG,
                                "Client could not cast message "
                                        + "ID=" + getId()
                        );

                    }
                }
            }
        });
        readThread.start();
    }

    @Override
    public void sendMessage(RBHproto.RBHMessage message){
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            Log.add(DEBUG_TAG,
                    "Client could not write message" +
                            "ID=" + getId());
            closeConnection();
            e.printStackTrace();
        }
    }


    //==========================================================================
    // Connection state handling
    //==========================================================================


    @Override
    public void onConnectionClosed(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            Log.add(DEBUG_TAG,
                    "Client could not close connection" +
                            "ID=" + getId()
            );
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionDenied(String reason){
        RBHproto.RBHMessage m =
                ProtoFactory.buildAuthDeniedMessage(Config.SERVER_ID,
                        "REASON=" + reason);
        sendMessage(m);
    }

    @Override
    public void onConnectionAccepted(){
        RBHproto.RBHMessage m =
                ProtoFactory.buildAuthAcceptMessage(Config.SERVER_ID,
                        "Accepted client with id: "
                                + getId()
                                + " TIME= " + System.currentTimeMillis());
        sendMessage(m);
    }
}