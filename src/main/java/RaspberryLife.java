
import data.DataBaseHelper;
import data.Log;
import data.SerialConnector;
import server.RBLSocketServer;
import server.RBLWebSocketServer;
import util.Config;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Peter Mösenthin.
 *
 * Main class for the RaspberryLife server application.
 */
public class RaspberryLife {

    public static final String DEBUG_TAG = RaspberryLife.class.getSimpleName();
    public static Thread serverThread = null;
    public static Thread webServerThread = null;


    public static void main(String[] args){
        Log.printLogHeader();
        Config.readConfig();
        //Config.dumpConfig();
        listIPAddresses();
        // RaspberryHome WebSocketServer
        webServerThread = new Thread(new Runnable() {
            public void run() {
                Log.add(DEBUG_TAG,
                        "Starting WebSocketServer");
                RBLWebSocketServer webServer = new RBLWebSocketServer();
                webServer.start();
            }
        });

        // RaspberryHome Application Server
        serverThread = new Thread(new Runnable() {
                public void run() {
                Log.add(DEBUG_TAG, "Starting Java socket server");
                RBLSocketServer server = new RBLSocketServer();
                server.start(Config.get().getInt("socket.java_port"));
            }
        });

        // Start threads
        webServerThread.start();
        serverThread.start();

        // Initialize the serial connector for module communication
        SerialConnector.init();

        // Initialize database
        DataBaseHelper.setUpInitial();

    }

    private static void listIPAddresses(){
        List<String> ipAddresses = new ArrayList<String>();
        int maxLength_ipv4 = 15;
        Enumeration e = null;
        //Get all network interfaces
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        //Check all network interfaces
        while(e!= null && e.hasMoreElements()){
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();

            //Check all addresses in current interface
            while (ee.hasMoreElements()){
                InetAddress i = (InetAddress) ee.nextElement();
                String address = i.getHostAddress();
                if(address.length() <= maxLength_ipv4){
                    if(!address.equals("127.0.0.1") && !address.equals("0:0:0:0:0:0:0:1")){
                        //Log.address(DEBUG_TAG, "RBLServer on machine: " + i.getHostAddress());
                        ipAddresses.add(n.getName() + " " + address);
                    }
                }
            }
        }

        Log.add(DEBUG_TAG, "Server running on " + ipAddresses.toString());
    }


}
