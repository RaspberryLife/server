package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="logic_receiver_table")
public class LogicReceiver {

    @Id
    @GeneratedValue
    @Column(name="logic_receiver_table_id")
    private int logic_receiver_table_id;

    // One-to-One
    @Column(name="logic_receiver_actuator_id")
    private int logic_receiver_actuator_id;

    @Column(name="logic_receiver_instruction")
    private int logic_receiver_instruction;

}
