package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="logic_receiver")
public class LogicReceiver {

    @Id
    @GeneratedValue
    @Column(name="logic_receiver_id")
    private int logic_receiver_id;

    @ManyToOne
    @JoinColumn(name="logic_id")
    private Logic logic;

    @ManyToOne
    @JoinColumn(name="actuator")
    private Actuator actuator;

    @OneToOne(mappedBy="logic_receiver", cascade=CascadeType.ALL)
    private Instruction instruction;

    public int getLogic_receiver_id() {
        return logic_receiver_id;
    }

    public void setLogic_receiver_id(int logic_receiver_id) {
        this.logic_receiver_id = logic_receiver_id;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

}
