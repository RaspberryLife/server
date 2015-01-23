package data.model;

import javax.persistence.*;

/**
 * Created by Peter Mösenthin.
 */
@Entity
@Table(name="condition_table")
public class Condition {

    @Id
    @GeneratedValue
    @Column(name="condition_table_id")
    private int condition_table_id;
}
