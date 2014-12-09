package event;

/**
 * Created by Peter Mösenthin.
 */
public class DataBaseEvent {

    public static final int START_SESSION = 0;
    public static final int RUN_TEST = 1;

    private int message;

    public DataBaseEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
