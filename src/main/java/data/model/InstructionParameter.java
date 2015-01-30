package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="instruction_parameter_table")
public class InstructionParameter {

    @Id
    @GeneratedValue
    @Column(name="instruction_parameter_table_id")
    private int instruction_table_id;

    @ManyToOne
    @JoinColumn(name="instruction_table_id")
    private int instruction_id;

    @Column(name="instruction_parameter_string_value")
    private int instruction_parameter_string_value;

    @Column(name="instruction_parameter_int_value")
    private int instruction_parameter_int_value;

}

