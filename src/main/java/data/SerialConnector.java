package data;

import client.ClientHandler;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import protobuf.ProtoFactory;
import util.Config;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the serial connection to send data to modules
 */
public class SerialConnector {

    public static final String PORT_NAME = "COM3";
    private static final SerialPort serialPort = new SerialPort(PORT_NAME);

    public static final String DEBUG_TAG = "SerialConnector";

    /**
     * Sets up the serial port and opens it.
     */
    public static void init(){
        try {
            Log.add(DEBUG_TAG,"Initializing serial port " + PORT_NAME);
            serialPort.openPort();//Open serial port
            serialPort.setParams(57600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS
                    + SerialPort.MASK_DSR;
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(new SerialPortReader());
        } catch (SerialPortException e) {
            Log.add(DEBUG_TAG,"Unable to setup serial port");
        }
    }

    /**
     * Send a string message via the serial connection.
     * @param message
     */
    public static void send(final String message){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Write data to port, in this case a "0" (zero)
                    serialPort.writeBytes(message.getBytes());
                }
                catch (SerialPortException ex) {
                    Log.add(DEBUG_TAG,"Failed to send message via serial port");
                }
            }
        });
        t.start();
    }

    /**
     * Class to handle incoming serial messages.
     */
    static class SerialPortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                //Read data, if 10 bytes available
                try {
                    byte buffer[] = serialPort.readBytes();
                    if(buffer.length != 0) {
                        String message = new String(buffer);
                        message = message.trim();
                        Log.add(DEBUG_TAG, "Received serial message: " + message);
                        ClientHandler.broadcastMessage(
                                ProtoFactory.buildPlainTextMessage(
                                        Config.SERVER_ID,
                                        "Serial connector received message: " +
                                                message
                                ));
                    }
                }catch (SerialPortException ex) {
                    Log.add(DEBUG_TAG, "Serial port failed on receiving " +
                            "message");
                }
            }
        }

    }
}
