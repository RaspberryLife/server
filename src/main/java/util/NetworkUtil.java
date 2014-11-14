package util;/*
 * Created by Peter Mösenthin on 13.11.2014.
 */

import data.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    public static final String DEBUG_TAG = NetworkUtil.class.getSimpleName();

    public static void listIPAddresses(){
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
