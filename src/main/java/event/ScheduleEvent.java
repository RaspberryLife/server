package event;

/**
 * Created by Peter Mösenthin.
 */
public class ScheduleEvent {

    public enum Type{
        START_TIME_LOG,
        START_RESOURCE_LOG,
        REBUILD_DATABASE
    }

    private Type type;
    private int interval;
    private String identity;

    public ScheduleEvent(Type type) {
        this.type = type;
    }

    public ScheduleEvent(String identity, int interval, Type type) {
        this.identity = identity;
        this.interval = interval;
        this.type = type;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
