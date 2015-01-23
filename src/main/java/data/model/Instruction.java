package data.model;

import javax.persistence.*;

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

}
