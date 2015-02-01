package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="rbl_logic_initiator")
public class LogicInitiator {

    @Id
    @GeneratedValue
    @Column(name="logic_initiator_id")
    private int logic_initiator_id;

    @ManyToOne(cascade=CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Logic logic;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="actuator")
    private Actuator actuator;

    @OneToOne(mappedBy="logic_initiator", cascade=CascadeType.ALL)
    private Condition condition;

    //----------------------------------------------------------------------------------------------
    //                                      GETTER & SETTER
    //----------------------------------------------------------------------------------------------

    public int getLogic_initiator_id() {
        return logic_initiator_id;
    }

    public void setLogic_initiator_id(int logic_initiator_id) {
        this.logic_initiator_id = logic_initiator_id;
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

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

}
