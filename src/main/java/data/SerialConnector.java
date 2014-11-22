package data;

import client.ClientHandler;
import jssc.*;
import protobuf.ProtoFactory;
import protobuf.RblProto;
import util.Config;
import util.Log;

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
                Log.add(DEBUG_TAG, "No serial port found.");
                return;
            }
            Log.add(DEBUG_TAG,"Initializing serial port " + mPortName);
            mSerialPort = new SerialPort(mPortName);
            mSerialPort.openPort();
            mSerialPort.setParams(Config.get().getInt("serial.baudrate"),
                    Config.get().getInt("serial.data_bits"),
                    Config.get().getInt("serial.stop_bits"),
                    Config.get().getInt("serial.parity"));
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
        Log.add(DEBUG_TAG,"Sending serial message " + message + " Length=" + message.getBytes().length);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    //Write data to port, in this case a "0" (zero)
                    if(mSerialPort != null){
                        mSerialPort.writeBytes(message.getBytes());
                    } else{
                        Log.add(DEBUG_TAG,"Failed to send message. Serial port was null");
                    }
                }
                catch (SerialPortException ex) {
                    Log.add(DEBUG_TAG,"Failed to send message" + ex);
                }
            }
        });
        t.start();
    }

    /**
     * Class to handle incoming serial messages.
     */
    private static class SerialPortReader implements SerialPortEventListener {
        int messageLength = Config.get().getInt("serial.message_byte_length");
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == messageLength){
                    try {
                        byte buffer[] = mSerialPort.readBytes();
                        if(buffer.length != 0) {
                            String message = new String(buffer);
                            message = message.trim();
                            Log.add(DEBUG_TAG, "Received serial message: " + message);
                            ClientHandler.broadcastMessage(
                                    ProtoFactory.buildPlainTextMessage(
                                            Config.get().getString("server.id"),
                                            RblProto.RBLMessage.MessageFlag.RESPONSE,
                                            "Serial connector received message: " +
                                                    message
                                    ));
                            // Save value to database
                            //int temp = Integer.parseInt(message.split(":")[4]);
                            //DataBaseHelper.writeTempData(temp);
                        }
                    }catch (SerialPortException ex) {
                        Log.add(DEBUG_TAG, "Serial port failed on receiving message." + ex);
                    }
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
