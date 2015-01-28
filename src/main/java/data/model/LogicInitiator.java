package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="logic_initiator_table")
public class LogicInitiator {

    @Id
    @GeneratedValue
    @Column(name="logic_initiator_table_id")
    private int logic_initiator_table_id;

    // One-to-One
    @Column(name="logic_initiator_actuator_id")
    private int logic_initiator_actuator_id;

    // One-to-One
    @Column(name="logic_initiator_condition")
    private int logic_initiator_condition;

}
