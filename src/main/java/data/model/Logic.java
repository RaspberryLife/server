package data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */

@Entity
@Table(name="logic_table")
public class Logic {

    public static final String EXECUTION_REQUIREMENT_SINGLE = "SINGLE";
    public static final String EXECUTION_REQUIREMENT_MAJORITY = "MAJORITY";
    public static final String EXECUTION_REQUIREMENT_ALL = "ALL";

    @Id
    @GeneratedValue
    @Column(name="logic_id")
    private int logic_id;

    @Column(name="logic_name")
    private String name;

    @OneToOne(mappedBy="logic", cascade=CascadeType.ALL)
    private ExecutionFrequency execution_frequency;

    @Column(name="logic_execution_requirement")
    private String execution_requirement;

    @OneToMany(mappedBy="logic", cascade=CascadeType.ALL)
    private Set<LogicInitiator> logic_initiator = new HashSet<LogicInitiator>();

    @OneToMany(mappedBy="logic", cascade=CascadeType.ALL)
    private Set<LogicReceiver> logic_receiver = new HashSet<LogicReceiver>();

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

    public ExecutionFrequency getExecution_frequency() {
        return execution_frequency;
    }

    public void setExecution_frequency(ExecutionFrequency execution_frequency) {
        this.execution_frequency = execution_frequency;
    }

    public String getExecution_requirement() {
        return execution_requirement;
    }

    public void setExecution_requirement(String execution_requirement) {
        this.execution_requirement = execution_requirement;
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
