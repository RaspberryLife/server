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
    @Column(name="actuator_id")
    private int actuator_id;

    @Column(name="name")
    private String actuator_name;

    @Column(name="type")
    private String type;

    @OneToMany(mappedBy="actuator")
    private Set<LogicInitiator> logic_initiator;

    @OneToMany(mappedBy="actuator")
    private Set<LogicReceiver> logic_receiver;

    public int getActuator_id() {
        return actuator_id;
    }

    public void setActuator_id(int actuator_id) {
        this.actuator_id = actuator_id;
    }

    public String getActuator_name() {
        return actuator_name;
    }

    public void setActuator_name(String actuator_name) {
        this.actuator_name = actuator_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<LogicInitiator> getLogic_initiator() {
        return logic_initiator;
    }

    public void setLogic_initiator(Set<LogicInitiator> logic_initiator) {
        this.logic_initiator = logic_initiator;
    }

    public Set<LogicReceiver> getLogic_receiver() {
        return logic_receiver;
    }

    public void setLogic_receiver(Set<LogicReceiver> logic_receiver) {
        this.logic_receiver = logic_receiver;
    }
}
