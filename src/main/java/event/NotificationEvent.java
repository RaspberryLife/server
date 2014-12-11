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
    private String subject;

    public NotificationEvent(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public NotificationEvent(String subject, String message, Type type) {
        this.subject = subject;
        this.message = message;
        this.type = type;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
