package server.serial;

import com.google.common.eventbus.Subscribe;
import event.ModuleEvent;
import event.SystemEvent;
import jssc.*;
import event.SerialMessageEvent;
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

    private static final String PREFIX = "?";
    private static final String SUFFIX = "!";


    public final String DEBUG_TAG = SerialConnector.class.getSimpleName();

    public static void register(){
        EventBusService.register(instance);
    }

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
        EventBusService.register(new SerialMessageHandler());
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
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == mMessageLength){
                    try {
                        byte buffer[] = mSerialPort.readBytes();
                        if(buffer.length != 0) {
                            String message = new String(buffer);
                            message = message.trim().substring(1, mMessageLength - 2);
                            EventBusService.post(new SerialMessageEvent(message));
                        }
                    } catch (SerialPortException ex) {
                        Log.add(DEBUG_TAG, "Serial port failed on receiving message." + ex);
                    }
                }
            }
        }
    }

}
