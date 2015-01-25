package data.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="instruction_table")
public class Instruction {

    @Id
    @GeneratedValue
    @Column(name="instruction_table_id")
    private int instruction_table_id;

    @Column(name="instruction_id")
    private int instruction_id;

    @OneToMany(mappedBy="instruction_id")
    private Set<InstructionParameter> parameters;

    @Column(name="instruction_module_type")
    private int instruction_module_type;

    @Column(name="instruction_module_id")
    private int instruction_module_id;

}
