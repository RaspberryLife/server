package data.model;

/**
 * Created by Peter Mösenthin.
 */
public class Module {

    public static final String TYPE_OUTLET = "OUTLET";
    public static final String TYPE_PIR = "PIR";
    public static final String TYPE_REED = "REED";
    public static final String TYPE_TEMP = "TEMP";
    public static final String TYPE_LUMINOSITY = "LUMINOSITY";
    public static final String TYPE_RELAY = "RELAY";
    public static final String TYPE_PIR_AND_RELAY = "PIR_AND_RELAY";

    private int id;

    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
