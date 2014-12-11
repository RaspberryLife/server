package server.serial;

import com.google.common.eventbus.Subscribe;
import event.ModuleEvent;
import event.SerialMessageEvent;
import event.SystemEvent;
import jssc.*;
import system.service.EventBusService;
import data.Config;
import util.Log;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the serial connection to send data to modules
 */
public class SerialConnector {

    private static final SerialConnector instance = new SerialConnector();

    private SerialPort mSerialPort = null;
    private String mPortName = null;
    private int mMessageLength;
    private String mOverridePort = null;

    private SerialMessageHandler mMessageHandler;

    private static final String PREFIX = "?";
    private static final String SUFFIX = "!";

    public final String DEBUG_TAG = SerialConnector.class.getSimpleName();

    public static void register(){
        EventBusService.register(instance);
    }

    @Subscribe
    public void handleEvent(SystemEvent e){
        switch (e.getType()){
            case START_SERIAL_CONNECTION:
                start();
                break;
            case STOP_SERIAL_CONNECTION:
                stop();
                break;
            case RESTART_SERIAL_CONNECTION:
                restart();
        }
    }

    private SerialConnector(){
    }

    /**
     * Sets up the serial port and opens it.
     */
    private void start(){
        Log.add(DEBUG_TAG, "Starting...");
        mPortName = determinePortName();
        if(mPortName == null){
            Log.add(DEBUG_TAG, "No serial port found");
            return;
        }
        mMessageLength = Config.getConf().getInt("serial.message_byte_length");
        mMessageHandler = new SerialMessageHandler();
        EventBusService.register(mMessageHandler);
        try {
            Log.add(DEBUG_TAG,"Opening serial port " + mPortName);
            mSerialPort = new SerialPort(mPortName);
            mSerialPort.openPort();
            mSerialPort.setParams(Config.getConf().getInt("serial.baudrate"),
                    Config.getConf().getInt("serial.data_bits"),
                    Config.getConf().getInt("serial.stop_bits"),
                    Config.getConf().getInt("serial.parity"));
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS
                    + SerialPort.MASK_DSR;
            mSerialPort.setEventsMask(mask);
            mSerialPort.addEventListener(new SerialPortReader());
        } catch (SerialPortException e) {
            Log.add(DEBUG_TAG,"Unable to setup serial port. " + e);
        }
    }

    private void restart(){
        stop();
        start();
    }

    private void stop(){
        if(mSerialPort != null){
            try {
                mSerialPort.closePort();
                mSerialPort = null;
            } catch (SerialPortException e) {
                Log.add(DEBUG_TAG, "Unable to close serial port. " + e);
            }
        }
    }

    private String determinePortName(){
        String[] availablePorts = SerialPortList.getPortNames();
        String portName = null;
        for (String port : availablePorts) {
            Log.add(DEBUG_TAG, "Found serial Port " + port);
        }
        if(availablePorts.length > 0){
            portName = availablePorts[0];
        }
        mOverridePort = Config.getConf().getString("serial.override_port");
        if(!mOverridePort.isEmpty()){
            portName = mOverridePort;
        }
        return portName;
    }

    /**
     * Send a message via the serial connection.
     */
    @Subscribe
    public void send(ModuleEvent instruction){
        final String message = PREFIX + instruction.build() + SUFFIX;
        Log.add(DEBUG_TAG,"Sending serial message " + message
                + " Length=" + message.getBytes().length);

        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
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
    private class SerialPortReader implements SerialPortEventListener {
        private String currentRead = "";
        private byte buffer[];


        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                try {
                    buffer = mSerialPort.readBytes();
                } catch (SerialPortException e) {
                    Log.add(DEBUG_TAG, "Serial port failed on receiving message." + e);
                }
                String eventString = new String(buffer);
                currentRead += eventString;
                if(currentRead.length() >= mMessageLength){
                    Log.add(DEBUG_TAG,"Message >= 34: " + currentRead);
                    String f = currentRead;
                    int readEnd = filterEventString(f);
                    if(readEnd > -1){
                        currentRead = currentRead.substring(readEnd + 1, currentRead.length());
                        Log.add(DEBUG_TAG, "New buffer: " + currentRead);
                    }
                }
            }
        }
    }


    private int filterEventString(String e){
        if(e.contains(PREFIX) && e.contains(SUFFIX)){
            int pp = findFirstOccurrence(e, PREFIX);
            int ps = findFirstOccurrence(e, SUFFIX);
            Log.add(DEBUG_TAG, "Found delimiter: PRE@=" + pp + " SUF@=" + ps);
            String cleaned = e.substring(pp + 1,pp + 32);
            Log.add(DEBUG_TAG,"Cleaned String: " + cleaned);
            EventBusService.post(new SerialMessageEvent(cleaned));
            return ps;
        }
        return -1;
    }

    private int findFirstOccurrence(String s, String find){
        char[] compareSplit = s.toCharArray();
        char match = find.charAt(0);
        for(int i = 0; i < compareSplit.length; i++){
            if(compareSplit[i] == match){
                return i;
            }
        }
        return -1;
    }
}
