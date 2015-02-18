package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="rbl_module")
public class Module {

    public static final String TYPE_OUTLET = "OUTLET";
    public static final String TYPE_PIR = "PIR";
    public static final String TYPE_REED = "REED";
    public static final String TYPE_TEMP = "TEMP";
    public static final String TYPE_LUMINOSITY = "LUMINOSITY";
    public static final String TYPE_RELAY = "RELAY";
    public static final String TYPE_PIR_AND_RELAY = "PIR_AND_RELAY";

    @Id
    @GeneratedValue
    @Column(name="module_id")
    private int id;

    @Column(name="module_type")
    private String type;

    @Column(name="serial_address")
    private String serial_address;

    @Column(name="name")
    private String name;

    //----------------------------------------------------------------------------------------------
    //                                      GETTER & SETTER
    //----------------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial_address() {
        return serial_address;
    }

    public void setSerial_address(String serial_address) {
        this.serial_address = serial_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
