package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="instruction")
public class Instruction {

    @Id
    @GeneratedValue
    @Column(name="instruction_id")
    private int instruction_id;

    @Column(name="instruction_parameters")
    private String parameters;

    @OneToOne
    @PrimaryKeyJoinColumn
    private LogicInitiator logic_receiver;

    public int getInstruction_id() {
        return instruction_id;
    }

    public void setInstruction_id(int instruction_id) {
        this.instruction_id = instruction_id;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public LogicInitiator getLogic_receiver() {
        return logic_receiver;
    }

    public void setLogic_receiver(LogicInitiator logic_receiver) {
        this.logic_receiver = logic_receiver;
    }
}
