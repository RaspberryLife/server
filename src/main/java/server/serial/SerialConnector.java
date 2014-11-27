package server.serial;

import com.google.common.eventbus.Subscribe;
import jssc.*;
import message.SerialMessageHandler;
import message.events.ModuleInstruction;
import message.events.SerialMessage;
import system.EventBusService;
import util.Config;
import util.Log;

/**
 * Created by Peter Mösenthin.
 *
 * Class to handle the serial connection to send data to modules
 */
public class SerialConnector {

    private SerialPort mSerialPort = null;
    private String mPortName = null;
    private int messageLength;


    public final String DEBUG_TAG = SerialConnector.class.getSimpleName();

    /**
     * Sets up the serial port and opens it.
     */
    public void init(){
        mPortName = determinePortName();
        if(mPortName == null){
            Log.add(DEBUG_TAG, "No serial port found.");
            return;
        }
        messageLength = Config.getConf().getInt("serial.message_byte_length");
        EventBusService.register(new SerialMessageHandler());
        EventBusService.register(this);
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

    public void reset(){
        if(mSerialPort != null){
            try {
                mSerialPort.closePort();
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
        return portName;
    }

    /**
     * Send a message via the serial connection.
     */
    @Subscribe
    public void send(ModuleInstruction instruction){
        final String message = "?" + instruction.build() + "!";
        Log.add(DEBUG_TAG,"Sending serial message " + message
                + " Length=" + message.getBytes().length);

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
    private class SerialPortReader implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() == messageLength){
                    try {
                        byte buffer[] = mSerialPort.readBytes();
                        if(buffer.length != 0) {
                            String message = new String(buffer);
                            message = message.trim().substring(1, messageLength - 2);
                            EventBusService.post(new SerialMessage(message));
                        }
                    }catch (SerialPortException ex) {
                        Log.add(DEBUG_TAG, "Serial port failed on receiving message." + ex);
                    }
                }
            }
        }
    }

    public SerialPort getSerialPort() {
        return mSerialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        mSerialPort = serialPort;
    }

    public String getPortName() {
        return mPortName;
    }

    public void setPortName(String portName) {
        mPortName = mPortName;
    }
}
