package data.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="actuator")
public class Actuator {

    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_MODULE = "MODULE";
    public static final String TYPE_CLIENT = "CLIENT";

    @Id
    @GeneratedValue
    @Column(name="actuator_table_id")
    private int actuator_table_id;

    @Column(name="actuator_name")
    private String actuator_name;

    @ManyToMany(mappedBy="logic_initiator")
    private Set<Logic> logic_initiator;

    @ManyToMany(mappedBy="logic_receiver")
    private Set<Logic> logic_receiver;

    @Column(name="actuator_id", nullable=true)
    private int actuator_id;

    @Column(name="actuator_type")
    private String type;

    public int getActuator_id() {
        return actuator_id;
    }

    public void setActuator_id(int actuator_id) {
        this.actuator_id = actuator_id;
    }

    public Set<Logic> getLogic_initiator() {
        return logic_initiator;
    }

    public void setLogic_initiator(Set<Logic> logic_initiator) {
        this.logic_initiator = logic_initiator;
    }

    public Set<Logic> getLogic_receiver() {
        return logic_receiver;
    }

    public void setLogic_receiver(Set<Logic> logic_receiver) {
        logic_receiver = logic_receiver;
    }

    public int getActuator_table_id() {
        return actuator_table_id;
    }

    public void setActuator_table_id(int actuator_table_id) {
        this.actuator_table_id = actuator_table_id;
    }

    public String getType() {
        return type;
    }

    public String getActuator_name() {
        return actuator_name;
    }

    public void setActuator_name(String actuator_name) {
        this.actuator_name = actuator_name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
