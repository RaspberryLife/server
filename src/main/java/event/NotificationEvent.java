package event;

/**
 * Created by Peter Mösenthin.
 */
public class NotificationEvent {

    public enum Type{
        CLIENT_BROADCAST,
        CLIENT_EMAIL
    }

    private Type type;
    private String message;

    public NotificationEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
