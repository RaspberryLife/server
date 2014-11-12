package data;

import client.ClientHandler;
import com.adamtaft.eb.EventBusService;
import jssc.*;
import protobuf.ProtoFactory;
import util.Config;

import java.util.Comparator;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the serial connection to send data to modules
 */
public class SerialConnector {

    private static SerialPort mSerialPort = null;
    private static String mPortName = null;


    public static final String DEBUG_TAG = SerialConnector.class.getSimpleName();

    /**
     * Sets up the serial port and opens it.
     */
    public static void init(){
        try {
            mPortName = determinePortName();
            if(mPortName == null){
                return;
            }
            Log.add(DEBUG_TAG,"Initializing serial port " + mPortName);
            mSerialPort = new SerialPort(mPortName);
            mSerialPort.openPort();
            mSerialPort.setParams(Config.SERIAL_PORT_BAUDRATE,
                    Config.SERIAL_PORT_DATA_BITS,
                    Config.SERIAL_PORT_STOP_BITS,
                    Config.SERIAL_PORT_PARITY);
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS
                    + SerialPort.MASK_DSR;
            mSerialPort.setEventsMask(mask);
            mSerialPort.addEventListener(new SerialPortReader());
        } catch (SerialPortException e) {
            Log.add(DEBUG_TAG,"Unable to setup serial port. " + e);
        }
    }

    public static void reset(){
        if(mSerialPort != null){
            try {
                mSerialPort.closePort();
            } catch (SerialPortException e) {
                Log.add(DEBUG_TAG, "Unable to close serial port. " + e);
            }
        }

    }

    private static String determinePortName(){
        String[] availiablePorts = SerialPortList.getPortNames();
        String portName = null;
        for (String port : availiablePorts) {
            Log.add(DEBUG_TAG, "Found serial Port " + port);
        }
        if(availiablePorts.length > 0){
            portName = availiablePorts[0];
        }
        return portName;
    }

    /**
     * Send a string message via the serial connection.
     * @param message
     */
    public static void send(final String message){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    //Write data to port, in this case a "0" (zero)
                    if(mSerialPort != null){
                        mSerialPort.writeBytes(message.getBytes());
                    }
                }
                catch (SerialPortException ex) {
                    Log.add(DEBUG_TAG,"Failed to send message via serial port " + ex);
                }
            }
        });
        t.start();
    }

    /**
     * Class to handle incoming serial messages.
     */
    private static class SerialPortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                //Read data, if 10 bytes available
                try {
                    byte buffer[] = mSerialPort.readBytes(Config.SERIAL_MESSAGE_BYTE_LENGTH);
                    if(buffer.length != 0) {
                        String message = new String(buffer);
                        message = message.trim();
                        Log.add(DEBUG_TAG, "Received serial message: " + message
                                + " Bufferlength" + "=" + buffer.length);
                        ClientHandler.broadcastMessage(
                                ProtoFactory.buildPlainTextMessage(
                                        Config.SERVER_ID,
                                        "Serial connector received message: " +
                                                message
                                ));
                    }
                }catch (SerialPortException ex) {
                    Log.add(DEBUG_TAG, "Serial port failed on receiving message." + ex);
                }
            }
        }

    }

    public static SerialPort getSerialPort() {
        return mSerialPort;
    }

    public static void setSerialPort(SerialPort serialPort) {
        mSerialPort = serialPort;
    }

    public static String getPortName() {
        return mPortName;
    }

    public static void setPortName(String portName) {
        mPortName = mPortName;
    }
}
