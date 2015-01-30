package data.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="actuator_table")
public class Actuator {

    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_MODULE = "MODULE";
    public static final String TYPE_CLIENT = "CLIENT";

    @Id
    @GeneratedValue
    @Column(name="actuator_id")
    private int actuator_id;

    @Column(name="name")
    private String name;

    @Column(name="type")
    private String type;

    @OneToMany(mappedBy="actuator", cascade=CascadeType.ALL)
    private Set<LogicInitiator> logic_initiator;

    @OneToMany(mappedBy="actuator", cascade=CascadeType.ALL)
    private Set<LogicReceiver> logic_receiver;

    public int getActuator_id() {
        return actuator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
