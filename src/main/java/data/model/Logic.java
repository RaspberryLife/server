package data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name="logic")
public class Logic {

    @Id
    @GeneratedValue
    @Column(name="logic_table_id")
    private int logic_table_id;

    @Column(name="logic_id")
    private int logic_id;

    @Column(name="logic_name")
    private String name;

    // One-to-One
    @Column(name="logic_execution_frequency")
    private ExecutionFrequency logic_execution_frequency;

    @Column(name="logic_execution_requirement")
    private String logic_execution_requirement;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="logic_initiator",
            joinColumns={@JoinColumn(name="logic_table_id")},
            inverseJoinColumns={@JoinColumn(name="actuator_table_id")})
    private Set<Actuator> logic_initiator = new HashSet<Actuator>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="logic_receiver",
            joinColumns={@JoinColumn(name="logic_table_id")},
            inverseJoinColumns={@JoinColumn(name="actuator_table_id")})
    private Set<Actuator> logic_receiver = new HashSet<Actuator>();

    //private Condition condition;

    public int getLogic_table_id() {
        return logic_table_id;
    }

    public void setLogic_table_id(int logic_table_id) {
        this.logic_table_id = logic_table_id;
    }

    public int getLogic_id() {
        return logic_id;
    }

    public void setLogic_id(int logic_id) {
        this.logic_id = logic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Actuator> getLogic_initiator() {
        return logic_initiator;
    }

    public void setLogic_initiator(Set<Actuator> logic_initiator) {
        this.logic_initiator = logic_initiator;
    }

    public Set<Actuator> getLogic_receiver() {
        return logic_receiver;
    }

    public void setLogic_receiver(Set<Actuator> logic_receiver) {
        this.logic_receiver = logic_receiver;
    }
}
