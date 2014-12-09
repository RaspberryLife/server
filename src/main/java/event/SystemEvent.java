package event;

/**
 * Created by Peter Mösenthin.
 */
public class SystemEvent {

    public static final int START_SYSTEM = 0;
    public static final int STOP_SYSTEM = 1;
    public static final int RESTART_SYSTEM = 2;

    public static final int START_SOCKET_SERVER = 3;
    public static final int STOP_SOCKET_SERVER = 4;
    public static final int RESTART_SOCKET_SERVER = 5;

    public static final int START_WEB_SOCKET_SERVER = 6;
    public static final int STOP_WEB_SOCKET_SERVER = 7;
    public static final int RESTART_WEB_SOCKET_SERVER = 8;

    public static final int START_SERIAL_CONNECTION = 9;
    public static final int STOP_SERIAL_CONNECTION = 10;
    public static final int RESTART_SERIAL_CONNECTION = 11;


    private int message;

    public SystemEvent(int type){
        this.message = type;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
